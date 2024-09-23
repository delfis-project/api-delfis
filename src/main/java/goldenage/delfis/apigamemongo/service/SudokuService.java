/*
 * Classe SudokuService
 * Service da entidade Sudoku
 * Autor: João Diniz Araujo
 * Data: 16/09/2024
 * */

package goldenage.delfis.apigamemongo.service;

import goldenage.delfis.apigamemongo.model.Sudoku;
import goldenage.delfis.apigamemongo.repository.SudokuRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SudokuService {
    private final SudokuRepository sudokuRepository;

    public SudokuService(SudokuRepository sudokuRepository) {
        this.sudokuRepository = sudokuRepository;
    }

    /**
     * @return todos os sudokus do banco.
     */
    public List<Sudoku> getSudokus() {
        List<Sudoku> sudokus = sudokuRepository.findAll();
        return sudokus.isEmpty() ? null : sudokus;
    }

    /**
     * @return sudoku inserido.
     */
    public Sudoku saveSudoku(Sudoku sudoku) {
        return sudokuRepository.save(sudoku);
    }
}
