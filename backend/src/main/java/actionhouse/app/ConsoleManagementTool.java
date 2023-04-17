package actionhouse.app;

import actionhouse.app.bl.AnalyticsBl;
import actionhouse.app.bl.ArticleBl;
import actionhouse.app.bl.BidBl;
import actionhouse.app.bl.CustomerBl;
import actionhouse.app.enums.*;
import actionhouse.app.util.Seeder;
import actionhouse.backend.util.JpaUtil;

import static actionhouse.app.util.MenuHelper.*;

public class ConsoleManagementTool {

    public static void main(String[] args) {
        ConsoleManagementTool consoleManagementTool = new ConsoleManagementTool();
        consoleManagementTool.run();
    }

    public void run() {
        System.out.println("--- ActionHouse Console Management Tool ---");

        Seeder.setupDatabase();

        Menu menu;
        do {
            menu = readMenu();
            switch (menu) {
                case CUSTOMER:
                    CustomerMenu customerMenu;
                    do {
                        customerMenu = readCustomerMenu();
                        customerAction(customerMenu);
                    } while (customerMenu != CustomerMenu.EXIT);
                    break;
                case ARTICLE:
                    ArticleMenu articleMenu;
                    do {
                        articleMenu = readArticleMenu();
                        articleAction(articleMenu);
                    } while (articleMenu != ArticleMenu.EXIT);
                    break;
                case BIDS:
                    BidMenu bidMenu;
                    do {
                        bidMenu = readBidMenu();
                        bidAction(bidMenu);
                    } while (bidMenu != BidMenu.EXIT);
                    break;
                case ANALYTICS:
                    AnalyticsMenu analyticsMenu;
                    do {
                        analyticsMenu = readAnalyticsMenu();
                        analyticsAction(analyticsMenu);
                    } while (analyticsMenu != AnalyticsMenu.EXIT);
                    break;
                case EXIT:
                    break;
            }
        } while (menu != Menu.EXIT);
    }

    private void customerAction(CustomerMenu customerMenu) {
        CustomerBl customerBl = new CustomerBl();

        switch (customerMenu) {
            case CREATE:
                customerBl.createCustomer();
                break;
            case UPDATE:
                customerBl.updateCustomer();
                break;
            case DELETE:
                customerBl.deleteCustomer();
                break;
            case SHOW:
                customerBl.showCustomer();
                break;
            case SHOW_ALL:
                customerBl.showCustomers();
                break;
            case EXIT:
                System.out.println("Exit Customer");
                break;
        }
    }

    private void articleAction(ArticleMenu articleMenu) {
        ArticleBl articleBl = new ArticleBl();

        switch (articleMenu) {
            case CREATE:
                articleBl.createArticle();
                break;
            case UPDATE:
                articleBl.updateArticle();
                break;
            case DELETE:
                articleBl.deleteArticle();
                break;
            case SHOW:
                articleBl.showArticle();
                break;
            case SHOW_ALL:
                articleBl.showArticles();
                break;
            case EXIT:
                System.out.println("Exit Article");
                break;
        }
    }

    private void bidAction(BidMenu bidMenu) {
        BidBl bidBl = new BidBl();

        switch (bidMenu) {
            case CREATE:
                bidBl.createBid();
                break;
            case DELETE:
                bidBl.deleteBid();
                break;
            case EXIT:
                System.out.println("Exit Bid");
                break;
        }
    }

    private void analyticsAction(AnalyticsMenu analyticsMenu) {
        AnalyticsBl analyticsBl = new AnalyticsBl();
        switch(analyticsMenu) {
            case FIND_ARTICLE_BY_DESCRIPTION:
                analyticsBl.findArticleByDescription();
                break;
            case GET_ARTICLE_PRICE:
                analyticsBl.getArticlePrice();
                break;
            case GET_TOP_SELLERS:
                analyticsBl.getTopSellers();
                break;
            case GET_TOP_ARTICLES:
                analyticsBl.getTopArticles();
                break;
            case EXIT:
                System.out.println("Exit Analytics");
                break;
        }
    }

}
