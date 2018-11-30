package com.kztp.testengine.controller;

import com.kztp.testengine.exception.UnauthorizedRequestException;
import com.kztp.testengine.model.EditNews;
import com.kztp.testengine.model.NewNews;
import com.kztp.testengine.model.News;
import com.kztp.testengine.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
public class NewsController {
    @Autowired
    private NewsService newsService;
    @GetMapping("/news")
    public Page<News> getNews(@RequestParam("page") int pageNumber,
                              @RequestParam("pagesize") int pageSize){
        return newsService.getAllNews(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC,"id"));
    }
    @PostMapping("/admin/news/create")
    public void createNews(@RequestBody NewNews news) throws UnauthorizedRequestException {
        newsService.createNews(news);
    }
    @DeleteMapping("/admin/news/delete/{id}")
    public void deleteNews(@PathVariable("id") int id) throws UnauthorizedRequestException {
        newsService.deleteNews(id);
    }

    @PutMapping("/admin/news/edit")
    public void editNews(@RequestBody EditNews editNews) throws UnauthorizedRequestException {
        newsService.editNews(editNews);
    }
}
