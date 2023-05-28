import {Component, OnInit} from '@angular/core';
import {ArticleDto, BidDto, ArticlesService, BidService} from "../api-client";
import {FormControl, FormGroup, ValidationErrors, Validators} from "@angular/forms";
import {HttpHeaders} from "@angular/common/http";
import {ActivatedRoute, Route} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-article-detail',
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.css']
})
export class ArticleDetailComponent implements OnInit {
  article: ArticleDto = {} as ArticleDto;
  bidForm: FormGroup;
  user = localStorage.getItem('token')!;

  constructor(private articleService: ArticlesService,
              private route: ActivatedRoute,
              private bidService: BidService,
              private toastr: ToastrService) {
    this.bidForm = new FormGroup({
      'bidValue': new FormControl(null, [Validators.required, this.bidHigherThanPrice.bind(this)])
    });
  }

  ngOnInit() {
    this.fetchArticle();
  }

  deleteArticle() {
    this.articleService.deleteArticle(this.user, this.article.id!).subscribe(res => {
      this.toastr.success(`You deleted the article ${this.article.description}`, 'Article deleted');
      window.location.href = '/articles';
    });
  }

  placeBid() {
    // Place the bid
    const value = this.bidForm.get('bidValue')?.value;
    if (!value) {
      alert("Something is messed up with validation. Please flame your local developer.");
      return;
    }

    const bid = {amount: value, articleId: this.article.id, bidder: this.user} as BidDto;
    this.bidService.bid(this.user, bid).subscribe(res => {
      this.toastr.success(`You bid ${value} on the article ${this.article.description}`, 'Bid placed');
      this.fetchArticle();
    });

  }

  openAuction() {
    this.articleService.startAuction(this.user, this.article.id!, 'START').subscribe(res => {
      this.toastr.success(`You opened the auction for ${this.article.description}`, 'Auction opened');
      this.fetchArticle();
    });
  }

  closeAuction() {
    this.articleService.startAuction(this.user, this.article.id!, 'STOP').subscribe(res => {
      this.toastr.success(`You closed the auction for ${this.article.description}`, 'Auction closed');
      this.fetchArticle();
    });
  }

  bidHigherThanPrice(control: FormControl): ValidationErrors | null {
    if (!control.value || !this.article || control.value <= this.article?.price!) {
      return {'priceError': true};
    }
    return null;
  }

  private fetchArticle() {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/json');
    const articleId = Number.parseInt(this.route.snapshot.paramMap.get('id')!);

    this.articleService.getArticlesRunning(this.user).subscribe(
      response => {
        const article = response.find(article => article.id == articleId)!;
        if (!article) {
          this.toastr.error(`Could not fetch article`, 'Error');
          window.location.href = '/articles';
          return;
        }
        this.article = response.find(article => article.id == articleId)!;
      }
    );
  }
}
