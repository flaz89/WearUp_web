import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ProductService } from 'src/app/services/product.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {

  token:string = this.authService.token();
  isLoading:boolean = false;

  productsList: any[] = [];
  paginationInfo:any;

  searchForm!: FormGroup;

  brands:any = [];
  types:any = [];
  minPrices = ['0', '50', '100', '200'];
  maxPrices = ['50', '100', '200', '300', '400+'];

  constructor(private fb: FormBuilder, private authService:AuthService, private productSrv:ProductService ) { }

  ngOnInit(): void {

    this.searchForm = this.fb.group({
      productCode: [''],
      productName: [''],
      brand: [''],
      type: [''],
      minPrice: [''],
      maxPrice: [''],
      orderBy: ['creationDate']
    });

    this.getAllProducts(this.token);


  }
// --------------------------------- metodi

  addLike(product: any){
    product.isLiked = !product.isLiked;
    if (product.isLiked) {
      console.log("Hai messo mi piace a: " + product.id);
      // Chiamata HTTP per aggiungere il prodotto all'elenco dei "Mi piace"
    } else {
      console.log("Hai rimosso il mi piace a: " + product.brand.brandName);
      // Chiamata HTTP per rimuovere il prodotto dall'elenco dei "Mi piace"
    }
  }

  getAllProducts(token: string, page?: number, size?: number) {
    this.isLoading = true;
    this.productSrv.getAllProducts(token, page, size)
      .subscribe(
        (response: any) => {
          //array prodotti
          this.productsList = response.content;
          this.productsList = this.productsList.map(product => ({
            ...product,
            showTextProduct: false,
            isLiked: false
          }));
          console.log(this.productsList);

          //array brands
          const uniqueBrands: { [key: string]: any } = {};
          const uniqueBrandsArray: any[] = [];

          this.productsList.forEach(product => {
            const brandName = product.brand.brandName;
            const profilePicture = product.brand.profilePicture;

            if (!uniqueBrands[brandName]) {
              uniqueBrands[brandName] = {
                brandName: brandName,
                profilePicture: profilePicture
              };

              uniqueBrandsArray.push(uniqueBrands[brandName]);
            }
          });
          uniqueBrandsArray.sort((a, b) => {
            if (a.brandName < b.brandName) {
              return -1;
            }
            if (a.brandName > b.brandName) {
              return 1;
            }
            return 0;
          });
          this.brands = uniqueBrandsArray;

          console.log(this.brands);

          // array categorie
          this.types = Array.from(new Set(this.productsList.map(product => product.type)));
          console.log(this.types);

          //
          this.paginationInfo = {
            totalPages: response.totalPages,
            totalElements: response.totalElements,
          };
          setTimeout(() => {
            this.isLoading = false;
          }, 1500);
        },
        error => {
          console.log(error);
          setTimeout(() => {
            this.isLoading = false;
          }, 1500);
        }
      );
  }

  listproductByBrand(brandName: string) {
    this.isLoading = true;
    this.productSrv.getProductsByBrand(this.token, brandName).subscribe(
      (response: any) => {
        // Gestisci la risposta qui, ad esempio aggiornando un array di prodotti
        this.productsList = response.content;
        setTimeout(() => {
          this.isLoading = false;
        }, 1500);
      },
      error => {
        console.log(error);
      }
    );
  }

  createNewProduct(){
    //vado alla pagina per creare un nuovo prodotto
  }

  onSubmit(token:string){
    this.isLoading = true;
    this.productSrv.getProductbySearch(token, this.searchForm.value)
      .subscribe(
        (response:any) => {
          console.log('Risposta ricevuta:', response);
          this.productsList = response.content;
          this.searchForm.reset({
            productCode: '',
            productName: '',
            brand: '',
            type: '',
            minPrice: '',
            maxPrice: '',
            orderBy: 'creationDate'});
            setTimeout(() => {
              this.isLoading = false;
            }, 1500);
        },
        (error) => {
          console.error('Errore:', error);
          this.isLoading = false;
        }
      );


  }

}
