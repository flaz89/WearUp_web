import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, of, tap, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Product } from '../model/product.interface';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  baseURL = environment.baseURL;

  constructor(private http:HttpClient) { }

  //----------------------------------------------

  getAllProducts(token: string, page?: number, size?: number) {
    const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);

    let params = new HttpParams();
    if (page != null) params = params.append('page', page.toString());
    if (size != null) params = params.append('size', size.toString());

    return this.http.get(this.baseURL + 'products/all', { headers, params });
  }

  //----------------------------------------------

  gettAllCategories(token:string){
    const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);

    return this.http.get(this.baseURL + 'products/categories', { headers});
  }

  //----------------------------------------------

  getProductsByBrand(token: string, brandName: string) {
    const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);
    let params = new HttpParams().set('brand', brandName);

    return this.http.get(this.baseURL + 'products/search', { headers, params });
  }

  //----------------------------------------------

  getProductbySearch(token: string, searchFormValue: any) {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    let params = new HttpParams();

    const addParamIfPresent = (key: string, value: any) => {
      if (value !== null && value !== undefined && value !== '') {
        params = params.append(key, value);
      }
    };

    addParamIfPresent('productCode', searchFormValue.productCode);
    addParamIfPresent('productName', searchFormValue.productName);
    addParamIfPresent('brand', searchFormValue.brand);
    addParamIfPresent('type', searchFormValue.type);
    addParamIfPresent('minPrice', searchFormValue.minPrice);
    addParamIfPresent('maxPrice', searchFormValue.maxPrice);
    addParamIfPresent('sortBy', searchFormValue.orderBy);

    return this.http.get(this.baseURL + 'products/search', { headers, params });
}

//----------------------------------------------

  addToFav(userId: string, productId: number, token: string){
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const params = {
      userId: userId,
      productId: productId.toString()
    };

    return this.http.post(this.baseURL + "favorites",null, { headers, params } );
  }

 // ------------------------------------

  removeFavorite(userId: string, productId: number, token: string) {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const params = new HttpParams().set('productId', productId.toString());

    return this.http.delete(this.baseURL + "favorites/" + userId , { headers, params } );
  }

  // ------------------------------------

  getUserFavorites(userId:string, token:string){
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get(this.baseURL + "favorites/" + userId, { headers});
  }

   // ------------------------------------

  getProductById(productId:number, token:string){
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get(this.baseURL + "products/" + productId, { headers});
  }

  // ------------------------------------

  uploadProductImage(image: FormData, token:string): Observable<string> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.post<string>(this.baseURL + "products/upload-product-image", image, {headers}).pipe(
      tap((response:string) => {
      }),
      catchError((error:any) => {
        console.error('Error occurs during image uploading', error);
        return of('UNSUCCESSFUL');
      })
    );
  }

  // ------------------------------------

  uploadProduct3DModel(model3D:FormData, token:string): Observable<string>{
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    console.log(model3D);


    return this.http.post<string>(this.baseURL + "products/upload-3d-product", model3D, {headers}).pipe(
      tap((response:string) => {
      }),
      catchError((error:any) => {
        console.error('Error occurs during 3D model uploading', error);
        return of('UNSUCCESSFUL');
      })
    );
  }

  // ------------------------------------

  uploadProductTexture(texture: FormData, token: string, textureType: string): Observable<string> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    // Aggiungi textureType al FormData
    texture.append('textureType', textureType);

    return this.http.post<string>(this.baseURL + "products/upload-product-texture", texture, {headers}).pipe(
      tap((response: string) => {
        console.log('Texture uploaded:', response);
      }),
      catchError((error: any) => {
        console.error(`Error occurs during texture uploading`, error);
        return of('UNSUCCESSFUL');
      })
    );
  }

  createNewProduct(product: Product, token: string) {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<Product>(this.baseURL + "products", product, { headers })
      .pipe(
        catchError(error => {
          console.error('Error creating product:', error);
          return throwError(error);
        })
      );
  }

}
