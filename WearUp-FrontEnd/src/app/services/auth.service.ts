import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { UserRegister } from '../model/UserRegister.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseURL = environment.baseURL

  constructor(private http:HttpClient) { }

  uploadUserImage(formDataImage:FormData) {
    console.log("immagine prova caricamento");

    return this.http.post(this.baseURL + "auth/upload-user-image", formDataImage);
  }

  register(data:UserRegister) {
    return this.http.post(this.baseURL + "auth/register", data);
  }
}
