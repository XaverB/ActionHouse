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
import { PageMetadata } from './pageMetadata';
import { PagedModelEntityModelCustomerEmbedded } from './pagedModelEntityModelCustomerEmbedded';
import { Link } from './link';


export interface PagedModelEntityModelCustomer { 
    _embedded?: PagedModelEntityModelCustomerEmbedded;
    _links?: { [key: string]: Link; };
    page?: PageMetadata;
}

