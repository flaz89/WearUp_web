import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BrandService {

  constructor(private http:HttpClient) { }

  findBrandById(id:string, token: string){
    const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);
    return this.http.get("http://localhost:3001/brands/" + id, { headers });
  }
}
