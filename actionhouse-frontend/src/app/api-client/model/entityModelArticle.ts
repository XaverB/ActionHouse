/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { Link } from './link';


export interface EntityModelArticle { 
    description?: string;
    reservePrice?: number;
    hammerPrice?: number;
    auctionStartDate?: string;
    auctionEndDate?: string;
    status?: EntityModelArticle.StatusEnum;
    _links?: { [key: string]: Link; };
}
export namespace EntityModelArticle {
    export type StatusEnum = 'LISTED' | 'AUCTION_RUNNING' | 'SOLD' | 'NOT_SOLD';
    export const StatusEnum = {
        Listed: 'LISTED' as StatusEnum,
        AuctionRunning: 'AUCTION_RUNNING' as StatusEnum,
        Sold: 'SOLD' as StatusEnum,
        NotSold: 'NOT_SOLD' as StatusEnum
    };
}


