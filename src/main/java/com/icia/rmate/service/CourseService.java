package com.icia.rmate.service;

import com.icia.rmate.dto.CourseDTO;
import com.icia.rmate.dto.CourseEntity;
import com.icia.rmate.dao.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<CourseDTO> getCourseListByBNum(int bNum) {
        System.out.println("getCourseListByBNum 호출, bNum: " + bNum);
        List<CourseEntity> courseEntities = courseRepository.findByBNum(bNum);
        if (courseEntities.isEmpty()) {
            System.out.println("해당 게시글에 코스 정보가 없습니다. bNum: " + bNum);
            return java.util.Collections.emptyList();
        }

        return courseEntities.stream()
                .map(CourseDTO::toDTO)
                .collect(Collectors.toList());
    }


//        public void saveCourse(CourseDTO courseDTO) {
//            CourseEntity courseEntity = CourseEntity.toEntity(courseDTO);
//            courseRepository.save(courseEntity);
//        }

    public void addCourse(CourseEntity courseEntity) {
        courseRepository.save(courseEntity); // save 메서드 사용
    }
}