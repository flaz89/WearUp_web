import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { UserRegister } from '../model/UserRegister.interface';
import { BrandRegister } from '../model/BrandRegister.interface';
import { AccessLogin } from '../model/AccessLogin.interface';
import { AuthData } from '../model/auth-data.interface';
import { BehaviorSubject, tap } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { tokenGetter } from '../app.module';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseURL = environment.baseURL
  user!:AuthData;

  private authSubj = new BehaviorSubject<null | AuthData>(null);
  user$ = this.authSubj.asObservable();


  private profilePictureSubject = new BehaviorSubject<string | null>(null);
  profilePicture$ = this.profilePictureSubject.asObservable();



  constructor(private http:HttpClient, private jwtHelper:JwtHelperService, ) {
    const storedAuthData = localStorage.getItem('WU-Token');
    if (storedAuthData) {
      this.authSubj.next(JSON.parse(storedAuthData));
    } else {
      this.authSubj.next(null);
    }
    }


    uploadUserImage(formDataImage:FormData) {
      return this.http.post(this.baseURL + "auth/upload-user-image", formDataImage);
    }

    uploadBrandImage(formDataImage:FormData) {
      return this.http.post(this.baseURL + "auth/upload-brand-image", formDataImage);
    }

    registerUser(data:UserRegister) {
      return this.http.post(this.baseURL + "auth/register", data);
    }

    resgisterBrand(data:BrandRegister) {
      return this.http.post(this.baseURL + "auth/register/brand", data);
    }

    login(data:AccessLogin){
      return this.http.post<AuthData>(this.baseURL + "auth/login", data).pipe(tap((data => {
        console.log(data);
        this.authSubj.next(data);
        this.user = data;
        localStorage.setItem("WU-Token", JSON.stringify(data.accessToken));

      })))
    }

    // -----------------------------------------------------------
    isUserLoggedIn(): boolean {
      return this.authSubj.getValue() !== null;
    }

    decodeToken(token: string): any {
      try {
      return this.jwtHelper.decodeToken(token);
    } catch (error) {
      console.error("Impossibile decodificare il token", error);
      return null;
    }
  }

  updateProfilePicture(url: string) {
    this.profilePictureSubject.next(url);
  }

  token():string {
    const token:string | null = tokenGetter();
    if(token) {
      const tokenHeader = token.slice(1, -1);
      return tokenHeader;
    }
    return "";
  }
}
