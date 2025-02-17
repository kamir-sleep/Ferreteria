package com.proyecto.Fer.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyecto.Fer.model.MenuModel;
import com.proyecto.Fer.repository.MenuRepository;
import com.proyecto.Fer.service.MenuService;
import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<MenuModel> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public Optional<MenuModel> findById(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public MenuModel save(MenuModel menu) {
        return menuRepository.save(menu);
    }

    @Override
    public void deleteById(Long id) {
        menuRepository.deleteById(id);
    }
}