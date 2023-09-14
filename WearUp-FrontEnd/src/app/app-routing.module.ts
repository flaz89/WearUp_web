import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { TryItComponent } from './components/try-it/try-it.component';
import { AboutComponent } from './components/about/about.component';
import { LogInComponent } from './auth/register/log-in.component';


const routes: Routes = [
  {path:"", redirectTo:"home", pathMatch:"full"},
  {path:"home", component:HomeComponent},
  {path:"try-it", component:TryItComponent},
  {path:"about", component:AboutComponent},
  {path:"login", component:LogInComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
