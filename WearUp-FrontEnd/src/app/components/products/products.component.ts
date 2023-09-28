import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ProductService } from 'src/app/services/product.service';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {

  token:string = this.authService.token();
  userType!:string;
  userId!:string;
  brandId!:number;

  isLoading:boolean = false;


  productsList: any[] = [];
  favoritesList: any[] = [];
  favouritesLoaded:boolean = false; ///////////
  paginationInfo:any;

  searchForm!: FormGroup;

  brands:any = [];
  types:any = [];
  minPrices = ['0', '50', '100', '200'];
  maxPrices = ['50', '100', '200', '300', '400+'];

  constructor(
    private fb: FormBuilder,
    private authService:AuthService,
    private productSrv:ProductService,
    private router:Router,
    ) { }

  ngOnInit(): void {

    const decodedToken = this.authService.decodeToken(this.token);
    this.userType = decodedToken.entityType;
    this.userId = decodedToken.sub;

    this.searchForm = this.fb.group({
      productCode: [''],
      productName: [''],
      brand: [''],
      type: [''],
      minPrice: [''],
      maxPrice: [''],
      orderBy: ['creationDate']
    });

    if(this.userType == "User") {
      this.isLoading = true;
      this.getUserFavorite(this.userId, this.token);
    } else {
      this.getAllProducts(this.token);
  }


  }
  // --------------------------------- metodi

  getUserFavorite(userId: string, token: string) {
    this.productSrv.getUserFavorites(userId, token).subscribe(
        (favorites: any) => {
            console.log('Preferiti ricevuti:', favorites);
            this.favoritesList = favorites;
            this.favouritesLoaded = true;
            this.getAllProducts(this.token);
        },
        error => {
            console.error('Errore nell\'ottenere i preferiti:', error);
            this.favouritesLoaded = false;
        }
    );
  }

  addLike(product: any){
    product.isLiked = !product.isLiked;
    if (product.isLiked) {
      this.productSrv.addToFav(this.userId, product.id, this.token).subscribe(response => {
        console.log("Hai messo mi piace a: " + product.id);
      console.log(response);
      });

    } else {
      this.productSrv.removeFavorite(this.userId, product.id, this.token).subscribe(response => {
        console.log("Hai rimosso il mi piace a: " + product.id);
        console.log(response);

        });
    }
  }

  getAllProducts(token: string, page?: number, size?: number) {
    this.isLoading = true;
    this.productSrv.getAllProducts(token, page, size)
      .subscribe(
        (response: any) => {

          const favoriteProductIds = this.favoritesList.map(favorite => favorite.productId);

          //array prodotti
          this.productsList = response.content;
          this.productsList = this.productsList.map(product => ({
            ...product,
            showTextProduct: false,
            isLiked: favoriteProductIds.includes(product.id)
          }));
          console.log("Lista prodotti: ",this.productsList);

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

          //console.log("Brands :",this.brands);

          // array categorie
          this.types = Array.from(new Set(this.productsList.map(product => product.type)));

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

  showProduct(product:any){
    console.log("hai selezionato questo prodotto: ",product);
    this.router.navigate(['/product', product.id]);
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
