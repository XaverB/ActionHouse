import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';

import {ApiModule, BASE_PATH} from './api-client/';
import {HttpClientModule} from '@angular/common/http';
import {LoginComponent} from './login/login.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterModule, Routes} from '@angular/router';
import {ArticleListComponent} from './article-list/article-list.component';
import { ArticleDetailComponent } from './article-detail/article-detail.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { ArticleCreateComponent } from './article-create/article-create.component';

const routes: Routes = [
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'articles', component: ArticleListComponent},
  {path: 'article/:id', component: ArticleDetailComponent},
  {path: 'create', component: ArticleCreateComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ArticleListComponent,
    ArticleDetailComponent,
    ArticleCreateComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ApiModule,
    FormsModule,
    RouterModule.forRoot(routes),
    ReactiveFormsModule,
    BrowserAnimationsModule, // required animations module
    ToastrModule.forRoot(), // ToastrModule added
  ],
  providers: [{provide: BASE_PATH, useValue: '/api'}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
