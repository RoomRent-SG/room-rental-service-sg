package com.thiha.roomrent.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.thiha.roomrent.dto.FilterKeywords;
import com.thiha.roomrent.model.MrtStation;
import com.thiha.roomrent.repository.StationRepository;
import com.thiha.roomrent.service.FilterService;

@Service
public class FilterServiceImpl implements FilterService{
    @Autowired
    private StationRepository stationRepository;

    @Override
    @Cacheable(value = "filter-keywords")
    public FilterKeywords getFilterKeywords() {
        FilterKeywords filterKeywords = new FilterKeywords();
        List<MrtStation> stations = stationRepository.findAll();
        List<String> stationNames = stations.stream().map(station -> station.getName()).collect(Collectors.toList());
        filterKeywords.setStationNames(stationNames);
        return filterKeywords;
    }
    
}
