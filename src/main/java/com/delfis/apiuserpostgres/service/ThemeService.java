/*
 * Classe ThemeService
 * Service da entidade Theme
 * Autor: João Diniz Araujo
 * Data: 15/08/2024
 * */

package com.delfis.apiuserpostgres.service;

import com.delfis.apiuserpostgres.model.Theme;
import com.delfis.apiuserpostgres.repository.ThemeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    /**
     * @return todos os themes do banco.
     */
    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    /**
     * @return theme pelo nome.
     */
    public Theme getThemeByName(String name) {
        Optional<Theme> theme = themeRepository.findThemeByNameEqualsIgnoreCase(name);
        return theme.orElse(null);
    }

    /**
     * @return theme pelo id.
     */
    public Theme getThemeById(Long id) {
        Optional<Theme> theme = themeRepository.findById(id);
        return theme.orElse(null);  // vai retornar theme se ele existir, se não retorna null
    }

    /**
     * @return theme deletado.
     */
    public Theme deleteThemeById(Long id) {
        Theme theme = getThemeById(id);
        if (theme != null) themeRepository.deleteById(id);
        return theme;
    }

    /**
     * @return theme inserido.
     */
    public Theme saveTheme(Theme theme) {
        return themeRepository.save(theme);
    }
}
