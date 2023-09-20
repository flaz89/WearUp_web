import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

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
addToFav(){

}

}
