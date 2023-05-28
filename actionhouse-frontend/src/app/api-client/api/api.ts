export * from './articles.service';
import { ArticlesService } from './articles.service';
export * from './auth.service';
import { AuthService } from './auth.service';
export * from './bid.service';
import { BidService } from './bid.service';
export const APIS = [ArticlesService, AuthService, BidService];
