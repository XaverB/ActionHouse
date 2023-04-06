package actionhouse.backend.orm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter @Setter @ToString
public class Customer {

    @Id @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;
    private String email;

    @AttributeOverride(name="street", column = @Column(name="ship_street"))
    @AttributeOverride(name="city", column = @Column(name="ship_city"))
    @AttributeOverride(name="state", column = @Column(name="ship_state"))
    @AttributeOverride(name="zipCode", column = @Column(name="ship_zipcode"))
    @AttributeOverride(name="country", column = @Column(name="ship_country"))
    @Embedded
    private Address shippingAddress;

    @AttributeOverride(name="street", column = @Column(name="pay_street"))
    @AttributeOverride(name="city", column = @Column(name="pay_city"))
    @AttributeOverride(name="state", column = @Column(name="pay_state"))
    @AttributeOverride(name="zipCode", column = @Column(name="pay_zipcode"))
    @AttributeOverride(name="country", column = @Column(name="pay_country"))
    @Embedded
    private Address paymentAddress;

    // cascading all operations is fine here, because we want to delete
    // payment options when we delete a customer and so on
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<PaymentOption> paymentOptions = new HashSet<>();

    // we do not want to delete articles if we delete a customer
    // therefore we are just cascading PERSIST and MERGE
    @OneToMany(mappedBy = "seller", cascade = {CascadeType.REFRESH}, orphanRemoval = true)
    @ToString.Exclude
    private Set<Article> soldArticles = new HashSet<>();

    // we do not want to delete articles if we delete a customer
    // therefore we are just cascading PERSIST and MERGE
    @OneToMany(mappedBy = "buyer", cascade = {CascadeType.REFRESH}, orphanRemoval = true)
    @ToString.Exclude
    private Set<Article> boughtArticles = new HashSet<>();


    // I thought about not declaring this property, but all bids of a customer could be quite interesting
    @OneToMany(mappedBy = "bidder", cascade = CascadeType.REFRESH, orphanRemoval = true)
    @ToString.Exclude
    private Set<Bid> bids = new HashSet<>();

    public Customer(Long id, String firstname, String lastname, String email, Address shippingAddress, Address paymentAddress) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.shippingAddress = shippingAddress;
        this.paymentAddress = paymentAddress;
    }

    /**
     * Adds the payment option to the customer and sets the customer of the payment option to this customer
     */
    public void addPaymentOption(PaymentOption paymentOption) {
        if(paymentOption == null) {
            throw new IllegalArgumentException("paymentOption must not be null");
        }

        if(paymentOption.getCustomer() != null) {
            paymentOption.getCustomer().removePaymentOption(paymentOption);
        }
        paymentOption.setCustomer(this);
        paymentOptions.add(paymentOption);
    }

    /**
     * Removes the payment option from the customer and sets the customer of the payment option to null
     */
    public boolean removePaymentOption(PaymentOption paymentOption) {
        if(paymentOptions.remove(paymentOption)) {
            paymentOption.setCustomer(null);
            return true;
        }
        return false;
    }

    /**
     * Adds the article to the customer and sets the customer of the article to this customer
     */
    public void addSoldArticle(Article article) {
        if(article == null) {
            throw new IllegalArgumentException("article must not be null");
        }

        if(article.getSeller() != null) {
            article.getSeller().removeSoldArticle(article);
        }
        article.setSeller(this);
        soldArticles.add(article);
    }

    /**
     * Removes the article from the customer and sets the customer of the article to null
     */
    public boolean removeSoldArticle(Article article) {
        if(soldArticles.remove(article)) {
            article.setSeller(null);
            return true;
        }
        return false;
    }

    /**
     * Adds the article to the customer and sets the customer of the article to this customer
     */
    public void addBoughtArticle(Article article) {
        if(article == null) {
            throw new IllegalArgumentException("article must not be null");
        }

        if(article.getBuyer() != null) {
            article.getBuyer().removeBoughtArticle(article);
        }
        article.setBuyer(this);
        boughtArticles.add(article);
    }

    /**
     * Removes the article from the customer and sets the customer of the article to null
     */
    public boolean removeBoughtArticle(Article article) {
        if(boughtArticles.remove(article)) {
            article.setBuyer(null);
            return true;
        }
        return false;
    }

    /**
     * Adds the bid to the customer and sets the customer of the bid to this customer
     */
    public void addBid(Bid bid) {
        if(bid == null) {
            throw new IllegalArgumentException("bid must not be null");
        }

        if(bid.getBidder() != null) {
            bid.getBidder().getBids().remove(bid);
        }
        bid.setBidder(this);
        bids.add(bid);
    }

    /**
     * Removes the bid from the customer and sets the customer of the bid to null
     */
    public boolean removeBid(Bid bid) {
        if (bids.remove(bid)) {
            bid.setBidder(null);
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() { return 42; }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Customer other = (Customer) obj;
        return id != null && id.equals(other.id);
    }

}
