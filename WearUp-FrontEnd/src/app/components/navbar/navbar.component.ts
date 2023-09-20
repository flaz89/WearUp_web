import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { tokenGetter } from 'src/app/app.module';
import { AuthService } from 'src/app/services/auth.service';
import { BrandService } from 'src/app/services/brand.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  public isUserLoggedIn!:boolean;
  profilepicture!:string;

  constructor(
    private authService: AuthService,
    private http:HttpClient,
    private router:Router) { }

  ngOnInit(): void {
    this.isUserLoggedIn = this.authService.isUserLoggedIn();

    this.authService.user$.subscribe(user => {
      this.isUserLoggedIn = user !== null;
    });

    this.authService.profilePicture$.subscribe(newProfilePicture => {
      if (newProfilePicture) {
        this.profilepicture = newProfilePicture;
      }
    });

    this.userData();

  }

  userData() {
    this.authService.user$.subscribe(async (user) => {
      if (user) {
        const token: string | null = tokenGetter();
        if (token) {
          const decodedToken = this.authService.decodeToken(token);
          const userId: string = decodedToken.sub;
          const userEntity: string = decodedToken.entityType;
          const headers = new HttpHeaders().set('Authorization', `Bearer ${JSON.parse(token)}`);

          console.log("user id: " + userId, ", User entity: " + userEntity);

          let apiEndpoint = '';

          switch (userEntity) {
            case 'User':
              console.log("sono un user");
              apiEndpoint = `http://localhost:3001/users/${userId}`;
              break;
            case 'Brand':
              console.log("sono un brand");
              apiEndpoint = `http://localhost:3001/brands/${userId}`;
              break;
            case 'Admin':
              console.log("sono un admin");
              apiEndpoint = `http://localhost:3001/users/${userId}`;
              break;
            default:
              console.error("Unknown user entity type");
              return;
          }

          try {
            console.log("faccio chiamata get");
            console.log(decodedToken);


            const response: any = await this.http.get(apiEndpoint, { headers }).toPromise();
            console.log("Response:", response);
            this.authService.updateProfilePicture(response.profilePicture);


          } catch (error) {
            console.error("HTTP Error:", error);
          }
        }
      }
    });
  }

  logout() {
    localStorage.removeItem("WU-Token");
    console.log("logout utente");
    this.router.navigate(['/home']);
  }




}
