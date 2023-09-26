import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Route, RouterModule } from '@angular/router';
import { JwtModule } from '@auth0/angular-jwt';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './components/home/home.component';
import { TryItComponent } from './components/try-it/try-it.component';
import { AboutComponent } from './components/about/about.component';
import { LogInComponent } from './auth/register/log-in.component';
import { FooterComponent } from './components/footer/footer.component';
import { UserComponent } from './auth/register/userRegister/user.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrandComponent } from './auth/register/brandRegister/brand.component';
import { LoginAccessComponent } from './auth/login-access/login-access.component';
import { ProductsComponent } from './components/products/products.component';
import { ShowProductComponent } from './components/show-product/show-product.component';
import { CreateProductComponent } from './components/create-product/create-product.component';
import { Canvas3DComponent } from './components/canvas3-d/canvas3-d.component';

export function tokenGetter() {
  return localStorage.getItem('WU-Token');
}

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    TryItComponent,
    AboutComponent,
    LogInComponent,
    FooterComponent,
    UserComponent,
    BrandComponent,
    LoginAccessComponent,
    ProductsComponent,
    ShowProductComponent,
    CreateProductComponent,
    Canvas3DComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    JwtModule.forRoot({
      config: {
        tokenGetter: tokenGetter
      }}),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
