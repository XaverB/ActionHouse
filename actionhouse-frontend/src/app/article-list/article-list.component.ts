import {Component, OnInit} from '@angular/core';
import {ArticleDto, ArticlesService} from "../api-client";
import {HttpContext, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.css']
})
export class ArticleListComponent implements OnInit {
  searchForm: FormGroup;
  articles: ArticleDto[] = [];

  constructor(private articlesService: ArticlesService) {
    this.searchForm = new FormGroup({
      'description': new FormControl(''),
      'status': new FormControl(''),
      'maxPrice': new FormControl('')
    });
  }

  ngOnInit() {
    this.fetchArticles();
  }

  onSearch() {
    console.log(this.searchForm.value);
    this.fetchArticles();
  }

  fetchArticles() {
    // TODO guard for email token
    const email = localStorage.getItem('token')!;

    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/json');

    const status = this.searchForm.get('status')?.value === 'ALL' ? null : this.searchForm.get('status')?.value;

    const maxPrice = this.searchForm.get('maxPrice')?.value;
    const description = this.searchForm.get('description')?.value;

    console.log("fetchArticles with status: " + status + " maxPrice: " + maxPrice + " description: " + description + " email: " + email);
    this.articlesService.getArticlesRunning(email, status, maxPrice, description).subscribe(
      response => {
          console.log("fetchArticles success");
          this.articles = response;
      }
    );


  }
}
