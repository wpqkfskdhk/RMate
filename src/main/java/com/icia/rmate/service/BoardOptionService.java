package com.icia.rmate.service;




import com.icia.rmate.dao.BoardRepository;
import com.icia.rmate.dto.SearchOptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class BoardOptionService {


    @Autowired
    private BoardRepository boardRepository;

//    public List<Object[]> searchBoards(SearchOptionDTO searchOptionDTO) {
//        return boardRepository.searchBoards2(
//                searchOptionDTO.getRegionId(),
//                searchOptionDTO.getAgeRange(),
//                searchOptionDTO.getMateType(),
//                searchOptionDTO.getCategoryType(),
//                searchOptionDTO.getKeyword()
//        );
//
//    }

}
