package com.example.demo.service;

import com.example.demo.dto.request.AddressDTO;
import com.example.demo.model.address.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final TownRepository townRepository;
    private final PostCodeRepository postCodeRepository;


    public AddressEntity createAddress(AddressDTO addressDTO) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setStreet(addressDTO.getStreet());
        addressEntity.setDescription(addressDTO.getDescription());
        // gets the thingy from the database otherwise creates a new thingy
        addressEntity.setCountry(countryRepository.findByName(addressDTO.getCountryName()).orElseGet(() -> countryRepository.save(new CountryEntity(addressDTO.getCountryName()))));
        addressEntity.setState(stateRepository.findByName(addressDTO.getStateName()).orElseGet(() -> stateRepository.save(new StateEntity(addressDTO.getStateName()))));
        addressEntity.setTown(townRepository.findByName(addressDTO.getTownName()).orElseGet(() -> townRepository.save(new TownEntity(addressDTO.getTownName()))));
        addressEntity.setPostCode(postCodeRepository.findByCode(addressDTO.getPostCodeCode()).orElseGet(() -> postCodeRepository.save(new PostCodeEntity(addressDTO.getPostCodeCode()))));

        addressRepository.save(addressEntity);
        return addressEntity;
    }
}