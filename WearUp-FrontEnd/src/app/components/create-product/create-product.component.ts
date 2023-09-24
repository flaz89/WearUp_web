import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { fbxFileValidator, imageFileValidator } from 'src/app/auth/register/uploadValidator';
import { ProductService } from 'src/app/services/product.service';
import { AuthService } from 'src/app/services/auth.service';
import { Observable, forkJoin } from 'rxjs';
import { Product } from 'src/app/model/product.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-product',
  templateUrl: './create-product.component.html',
  styleUrls: ['./create-product.component.scss']
})
export class CreateProductComponent implements OnInit {

  token:string = this.authService.token();

  isImageLoaded!:boolean;
  imageSrc: string = '';
  @ViewChild('fileInput') fileInput!: ElementRef;

  productCategories!:object;

  selectedImageFile: File | null = null;
  selectedModel3DFile: File | null = null;
  selectedTextureFiles:{[key: string]: File} = {};
  productImageForm!:FormGroup;
  productDetailsForm!:FormGroup;
  productTexturesForm!:FormGroup;
  product3DForm!:FormGroup;


  isLoading:boolean = false;
  serverMessageOk:boolean = false;
  serverMessageError:boolean = false;


  constructor(private fb:FormBuilder, private prodSrv:ProductService, private authService: AuthService, private router:Router) {}

  ngOnInit(): void {

    this.prodSrv.gettAllCategories(this.token).subscribe(
      (data) => {
        this.productCategories = data;
        console.log(this.productCategories);

      },
      (error) => {
        console.error('Errore nel recupero delle categorie:', error);
      }
    )

    this.productImageForm = this.fb.group({
      productImage:['', [imageFileValidator]]
    });

    this.productDetailsForm = this.fb.group({
      productCode: ['', Validators.required],
      productName: ['', Validators.required],
      description: ['', Validators.maxLength(400)],
      type: ['', Validators.required],
      productLink: ['', Validators.required],
      price: ['', Validators.required]
    });


    this.productTexturesForm = this.fb.group({
      albedoMap: ['', [Validators.required, imageFileValidator]],
      alphaMap: ['', [imageFileValidator]],
      normalMap: ['', [imageFileValidator]],
      roughnessMap: ['',[imageFileValidator]],
      metalnessMap: ['', [imageFileValidator]],
      aoMap: ['', [imageFileValidator]],
      heightMap: ['', [imageFileValidator]],
    });

    this.product3DForm = this.fb.group({
      link3D:['',[Validators.required, fbxFileValidator]]
    })

  }


  // -------------------------------

  cancelImage() {
    this.fileInput.nativeElement.value = '';
    this.productImageForm.get("profilePicture")?.reset;
    this.isImageLoaded = false;
    this.productImageForm.reset();
  }

  imageUploaded(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedImageFile = file;

      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.imageSrc = reader.result as string;
        this.isImageLoaded = true;
      };
    }
  }

  imageTextureUploaded(event: any, fieldName: string) {
    const file = event.target.files[0];
    if (file) {
      this.selectedTextureFiles[fieldName] = file;

      const reader = new FileReader();
      reader.readAsDataURL(file);
    }
  }

  model3DUploaded(event:any){
    const file = event.target.files[0];
    if (file) {
      this.selectedModel3DFile = file;
    }
  }

  onSubmit() {
    this.isLoading = true;

    if (
      this.productImageForm.valid
       && this.product3DForm.valid
       && this.productTexturesForm.valid
       && this.productDetailsForm.valid
      ) {
      // Inizializza un array per contenere gli Observable delle chiamate HTTP
      const uploads: Observable<any>[] = [];

      const textures: { [key: string]: string } = {
        albedoMap: '',
        alphaMap: '',
        normalMap: '',
        roughnessMap: '',
        metalnessMap: '',
        aoMap: '',
        heightMap: ''
      };

      // Preparo il FormData e aggiungo l'Observable per l'immagine del prodotto
      if (this.selectedImageFile) {
        const formDataProductImage = new FormData();
        formDataProductImage.append("file", this.selectedImageFile);
        uploads.push(this.prodSrv.uploadProductImage(formDataProductImage, this.token));
      }

      // Preparo il FormData e aggiungo l'Observable per il modello 3D
      if (this.selectedModel3DFile) {
        const formData3DModel = new FormData();
        formData3DModel.append("file", this.selectedModel3DFile);
        uploads.push(this.prodSrv.uploadProduct3DModel(formData3DModel, this.token));
      }

      // Preparo i FormData e aggiungo gli Observable per le textures
      const textureFields = ['albedoMap', 'alphaMap', 'normalMap', 'roughnessMap', 'metalnessMap', 'aoMap', 'heightMap'];
      textureFields.forEach(textureType => {
        const textureFile = this.selectedTextureFiles[textureType];
        if (textureFile) {
          const formDataTexture = new FormData();
          formDataTexture.append("file", textureFile);
          uploads.push(this.prodSrv.uploadProductTexture(formDataTexture, this.token, textureType));
        }
      });

      // Uso forkJoin per attendere che tutte le chiamate HTTP siano completate
      forkJoin(uploads).subscribe(responses => {
        let productImage = '';
        let link3D = '';

        // Itero sulle risposte
        responses.forEach((response:any) => {
          //console.log('Response:', response);
          const url = response.url;
          if (url.includes('product-image')) {
            productImage = url;
          } else if (url.includes('product-model')) {
            link3D = url;
          } else if (url.includes('product-texture')) {
            //console.log('Texture URL detected:', url);
            textureFields.forEach((field) => {
              if (url.includes(field)) {
                //console.log(`Assigning URL to texture ${field}:`, url);
                textures[field] = url;
              }
            });
          }
        });

        // Costruisco l'oggetto finale
        const finalObject:Product = {
          ...this.productDetailsForm.value,
          textures,
          productPicture: productImage,
          link3D,
        };

        // Invia l'oggetto finale al DB
        this.prodSrv.createNewProduct(finalObject, this.token).subscribe(
          (response) => {
            // Gestisci la risposta dal server, se necessario
            this.isLoading = false;
            this.serverMessageOk = true;
            console.log('Product successfully created:', response);
            setTimeout(() => {
              this.router.navigate(['/products']);
            }, 2000);
          },
          (error) => {
            // Gestisci eventuali errori dal server
            this.isLoading = false;
            this.serverMessageOk = false;
            console.error('Error creating product:', error);
          });
      }, error => {
        console.error(error);
        this.isLoading = false;
      });

    } else {
      this.isLoading = false;
      this.serverMessageError = true;
    }
  }



}
