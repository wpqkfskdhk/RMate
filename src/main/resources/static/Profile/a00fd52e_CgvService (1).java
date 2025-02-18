package com.icia.cgv.service;

import com.icia.cgv.dto.CgvDTO;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CgvService {

    private ModelAndView mav;


    public ModelAndView cgv() {
        mav = new ModelAndView();

        // EdgeDriver 설정
        WebDriverManager.edgedriver().setup();

        // Edge 옵션 설정
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless");     // UI 없이 실행
        options.addArguments("--disable-gpu");  // GPU 비활성화(성능 최적화)

        WebDriver driver = new EdgeDriver(options);


        List<CgvDTO> movieList = new ArrayList<>(); // 영화 정보를 저장할 리스트
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));  // 대기 객체 설정(5초)

        try{
            String url = "http://www.cgv.co.kr/movies/?lt=1&ft=0";
            driver.get(url);    // url 주소에 접속

            // List<WebElement> movies = driver.findElements(By.cssSelector("#contents > div.wrap-movie-chart > div.sect-movie-chart > ol > li"));

            // 영화 리스트가 로드될 때까지 대기
            List<WebElement> movies = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#contents > div.wrap-movie-chart > div.sect-movie-chart > ol > li")));
            //System.out.println("영화 갯수 : " + movies.size());

            // 영화 리스트를 반복 처리
            for(int i = 0; i < movies.size(); i++){
                WebElement movie = movies.get(i);

                // 영화 기본 정보(무비차트) 추출
                int rank = Integer.parseInt(movie.findElement(By.cssSelector("div.box-image > strong")).getText().substring(3));
                String title = movie.findElement(By.cssSelector("div.box-contents > a > strong")).getText();
                String perc = movie.findElement(By.cssSelector("div.box-contents > div > strong > span")).getText();
                double percent = Double.parseDouble(perc.substring(0, perc.length() - 1));
                String releaseDate = movie.findElement(By.cssSelector("div.box-contents > span.txt-info > strong")).getText().substring(0, 10);    // 2024-12-05
                String poster = movie.findElement(By.cssSelector("div.box-image > a > span > img")).getAttribute("src");
                String movieURL = movie.findElement(By.cssSelector("div.box-image > a")).getAttribute("href");

                CgvDTO movieInfo = new CgvDTO();
                movieInfo.setRank(rank);
                movieInfo.setTitle(title);
                movieInfo.setPercent(percent);
                movieInfo.setReleaseDate(releaseDate);
                movieInfo.setPoster(poster);
                movieInfo.setMovie_url(movieURL);

                movieList.add(movieInfo);
            }

            System.out.println("무비차트 리스트 확인");
            System.out.println(movieList);

            List<CgvDTO> cgvList = new ArrayList<>();

            for(CgvDTO detail : movieList){
                cgvList.add(movieDetail(detail));
            }

            System.out.println("최종 크롤링한 영화정보");
            System.out.println(cgvList);

        } catch(Exception e) {
            // 크롤링 중 전반적인 오류 처리
            System.err.println("크롤링 중 오류 발생 : " + e.getMessage());
        }


        mav.setViewName("index");
        return mav;
    }


    public CgvDTO movieDetail(CgvDTO detail){

        // EdgeDriver 설정
        WebDriverManager.edgedriver().setup();

        // Edge 옵션 설정
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless");     // UI 없이 실행
        options.addArguments("--disable-gpu");  // GPU 비활성화(성능 최적화)

        WebDriver driver = new EdgeDriver(options);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));  // 대기 객체 설정(5초)

        // 영화 상세페이지로 이동
        driver.get(detail.getMovie_url());

        // 상세 정보가 로드될 때까지 대기
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#select_main > div.sect-base-movie > div.box-contents > div.spec")));

        // 상세정보 추출
        String spec = driver.findElement(By.cssSelector("#select_main > div.sect-base-movie > div.box-contents > div.spec")).getText();
        String story = driver.findElement(By.cssSelector("#menu > div.col-detail > div.sect-story-movie")).getText();

        // 기존 detail에 spec, story 추가
        detail.setSpec(spec);
        detail.setStory(story);

        return detail;
    }
}





