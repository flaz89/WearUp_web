import { Component, OnInit } from '@angular/core';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-try-it',
  templateUrl: './try-it.component.html',
  styleUrls: ['./try-it.component.scss']
})
export class TryItComponent implements OnInit {

  products!:any[];
  selectedProduct!:any;

  showText:boolean = true;

  constructor(private productSrv: ProductService) { }

  ngOnInit(): void {
    this.productSrv.getTopProducts().subscribe(
      (products:any) => {
        this.products = products;
        console.log(products);

      },
      (error) => {
        console.error('Errore nel recupero dei prodotti', error);
      }
    )
  }

  open3d(product:any){
    this.showText = false;
    this.selectedProduct = product;
  }

}
