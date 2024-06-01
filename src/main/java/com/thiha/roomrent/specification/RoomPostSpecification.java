package com.thiha.roomrent.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import com.thiha.roomrent.dto.RoomPostSearchFilter;
import com.thiha.roomrent.model.RoomPost;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class RoomPostSpecification implements Specification<RoomPost>{
    private RoomPostSearchFilter searchFilter;

    public RoomPostSpecification(RoomPostSearchFilter searchFilter){
        this.searchFilter = searchFilter;
    }

    @Override
    @Nullable
    public Predicate toPredicate(@SuppressWarnings("null") Root<RoomPost> root, @SuppressWarnings("null") CriteriaQuery<?> query, @SuppressWarnings("null") CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if(searchFilter.getAirConTime()!=null){
            Predicate airConTimePredicate = criteriaBuilder.equal(root.get("airConTime"), searchFilter.getAirConTime());
            predicates.add(airConTimePredicate);
        }
        if(searchFilter.getCookingAllowance()!=null){
            Predicate cookingAllowancePredicate = criteriaBuilder.equal(root.get("cookingAllowance"), searchFilter.getCookingAllowance());
            predicates.add(cookingAllowancePredicate);
        }
        if (searchFilter.getLocation()!=null) {
            Predicate locationPredicate = criteriaBuilder.equal(root.get("location"), searchFilter.getLocation());
            predicates.add(locationPredicate);
        }
        if (searchFilter.getMaxPrice()!=null) {
            Predicate maxPricePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("price"), searchFilter.getMaxPrice());
            predicates.add(maxPricePredicate);
        }
        if (searchFilter.getMinPrice()!=null) {
            Predicate minPricePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("price"), searchFilter.getMinPrice());
            predicates.add(minPricePredicate);
        }
        if(searchFilter.getPropertyType()!=null){
            Predicate propertyTypePredicate = criteriaBuilder.equal(root.get("propertyType"), searchFilter.getPropertyType());
            predicates.add(propertyTypePredicate);
        }
        if(searchFilter.getRoomType()!=null){
            Predicate roomTypePredicate = criteriaBuilder.equal(root.get("roomType"), searchFilter.getRoomType());
            predicates.add(roomTypePredicate);
        }
        if(searchFilter.getSharePub()!=null){
            Predicate sharePubPredicate = criteriaBuilder.equal(root.get("sharePub"), searchFilter.getSharePub());
            predicates.add(sharePubPredicate);
        }
        if(searchFilter.getStationName()!=null){
            Predicate stationPredicate = criteriaBuilder.equal(root.get("stationName"), searchFilter.getStationName());
            predicates.add(stationPredicate);
        }
        if(searchFilter.getTotalPax()!=null){
            Predicate totalPaxPredicate = criteriaBuilder.equal(root.get("totalPax"), searchFilter.getTotalPax());
            predicates.add(totalPaxPredicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
    
}
