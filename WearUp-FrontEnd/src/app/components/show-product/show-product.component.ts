import { Component, NgZone, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { ProductService } from 'src/app/services/product.service';



@Component({
  selector: 'app-show-product',
  templateUrl: './show-product.component.html',
  styleUrls: ['./show-product.component.scss']
})
export class ShowProductComponent implements OnInit {

  token:string = this.authService.token();
  productId!: number;
  product!:any;

  model3DLink!:string;

  isLoading:boolean = false;

  constructor(private productSrv:ProductService,private route: ActivatedRoute, private authService:AuthService,private ngZone: NgZone) { }

  ngOnInit(): void {

    this.getProductId();
    this.getProduct(this.productId, this.token)



  }

  // -------------------------------------------

  getProductId(){
    this.isLoading = true;
    this.route.paramMap.subscribe(params => {
      const productIdParam = params.get('id');
        if (productIdParam !== null) {
          this.productId = +productIdParam;
        } else {
          console.error('ProductId is null');

        }
      }
    )
  }

  // -------------------------------------------

  getProduct(productId: number, token: string) {
    this.productSrv.getProductById(productId, token).subscribe(
      (product: any) => {
        console.log('Prodotto ricevuto:', product);
        this.product = product;
        setTimeout(() => {
          this.isLoading = false;
        }, 1500);
      },
      (error) => {
        console.error('Errore nel ricevere il prodotto:', error);
      }
    );
  }

  // -------------------------------------------

  navigateToProductSite(){
    if (this.product?.productLink) {
      window.open(this.product.productLink, '_blank');
    }
  }

  // ------------------------------------------- THREE.JS


}
