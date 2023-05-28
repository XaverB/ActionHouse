import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ArticlesService, CreateArticleDto} from "../api-client";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-article-create',
  templateUrl: './article-create.component.html',
  styleUrls: ['./article-create.component.css']
})
export class ArticleCreateComponent implements OnInit {
  createArticleForm: FormGroup;

  constructor(private fb: FormBuilder, private articleService: ArticlesService,
              private toastr: ToastrService) {
    this.createArticleForm = this.fb.group({
      description: ['', Validators.required],
      price: ['', Validators.required]
    });
  }

  ngOnInit() {

  }

  onSubmit() {
    if (!this.createArticleForm.valid) {
      this.toastr.warning('Please fill out all required fields', 'Form invalid');
      return;
    }

    if (this.createArticleForm.valid) {
      console.log(this.createArticleForm.value);

      const createArticleDto = {} as CreateArticleDto;
      createArticleDto.description = this.createArticleForm.value.description;
      createArticleDto.price = this.createArticleForm.value.price;

      this.articleService.createArticle(localStorage.getItem('token')!, createArticleDto).subscribe(res => {
        this.toastr.success(`You created the article ${this.createArticleForm.value.description}`, 'Article created');
        window.location.href = `/article/${res}`;
      });
    }
  }
}
