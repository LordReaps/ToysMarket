package com.toysmarket.toysmarket.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Post_cards")
public class Post_cards {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String card_name, card_desc, card_place;
    private int card_price;
    private int card_status;

    @Column(name = "card_author")
    private Integer card_author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_author", insertable = false, updatable = false)
    private User author;

    @Column(name = "card_data", nullable = false)
    private LocalDate cardData;

    public LocalDate getCardData() {
        return cardData;
    }

    public void setCardData(LocalDate cardData) {
        this.cardData = cardData;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCard_desc() {
        return card_desc;
    }

    public void setCard_desc(String card_desc) {
        this.card_desc = card_desc;
    }

    public String getCard_place() {
        return card_place;
    }

    public void setCard_place(String card_place) {
        this.card_place = card_place;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


    public int getCard_price() {
        return card_price;
    }

    public void setCard_price(int card_price) {
        this.card_price = card_price;
    }

    public int getCard_status() {
        return card_status;
    }

    public void setCard_status(int card_status) {
        this.card_status = card_status;
    }

    @OneToOne(mappedBy="card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User_Post_Card userPostCard;

    public User_Post_Card getUserPostCard() {
        return userPostCard;
    }

    public void setUserPostCard(User_Post_Card userPostCard) {
        this.userPostCard = userPostCard;
    }

    @OneToMany(mappedBy = "postCard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Post_cards_Image> images;

    public Set<Post_cards_Image> getImages() {
        return images;
    }

    public void setImages(Set<Post_cards_Image> images) {
        this.images = images;
    }

    public Post_cards_Image getFirstImage() {
        if (images != null && !images.isEmpty()) {
            return images.iterator().next();
        }
        return null;
    }

    public Set<Post_cards_Image> getAllImages() {
        return images != null ? images : Set.of();
    }

    public String getFirstImagePath() {
        Post_cards_Image firstImage = getFirstImage();
        return firstImage != null ? firstImage.getImagePath() : null;
    }

    public Set<String> getAllImagePaths() {
        if (images == null || images.isEmpty()) {
            return Set.of();
        }
        return images.stream().map(Post_cards_Image::getImagePath).collect(Collectors.toSet());
    }
}
