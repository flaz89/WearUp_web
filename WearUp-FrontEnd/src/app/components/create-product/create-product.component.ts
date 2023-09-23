import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { imageFileValidator } from 'src/app/auth/register/uploadValidator';
import { ProductService } from 'src/app/services/product.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-create-product',
  templateUrl: './create-product.component.html',
  styleUrls: ['./create-product.component.scss']
})
export class CreateProductComponent implements OnInit {

  token:string = this.authService.token();

  isImageLoaded!:boolean;
  imageSrc: string = '';

  productCategories!:object;

  productDetailsForm!:FormGroup;
  productImageForm!:FormGroup;
  productTexturesForm!:FormGroup;
  product3DForm!:FormGroup;


  constructor(private fb:FormBuilder, private prodSrv:ProductService, private authService: AuthService) {}

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

    // SISTEMARE I FORM


    this.productDetailsForm = this.fb.group({
      productCode: ['', Validators.required],
      productName: ['', Validators.required],
      description: [''],
      type: [null, Validators.required]
      //productWebsite: [''],
    });

    this.productTexturesForm = this.fb.group({
      albedoMap: [''],
      alphaMap: [''],
      normalMap: [''],
      roughnessMap: [''],
      metalnessMap: [''],
      aoMap: [''],
      heightMap: [''],
    });

    this.productImageForm = this.fb.group({
      productImage:['', [imageFileValidator]]
    });

    this.product3DForm = this.fb.group({
      link3D:['']
    })

  }


  // -------------------------------

  cancelImage() {
    // this.fileInput.nativeElement.value = '';
    // this.imageForm.get("profilePicture")?.reset;
    // this.isImageLoaded = false;
    // this.imageForm.reset();
  }

  imageUploaded(event: any) {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      this.imageSrc = reader.result as string;
      this.isImageLoaded = true;
    };
  }

  model3DUploaded($event:any){

  }

  onSubmit() {

  }

}
