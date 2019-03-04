package com.liu.controller;

import com.liu.domain.Book;
import com.liu.service.BookService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("books")
    @ResponseBody
    public String getBooks() throws IOException {

        List<Book> books = bookService.findAll();
        List<Document> documents = new ArrayList<>();
        for (Book book : books) {
            Document document = new Document();
            document.add(new TextField("id",String.valueOf(book.getId()),Field.Store.YES));
            document.add(new TextField("name",book.getName(),Field.Store.YES));
            document.add(new TextField("price",book.getPrice(),Field.Store.YES));
            /*document.add(new TextField("desc",book.getDesc(),Field.Store.YES));*/

            TextField descField = new TextField("desc",book.getDesc(),Field.Store.YES);
            if (5 == book.getId()){
                descField.setBoost(100f);
            }
            document.add(descField);
            documents.add(document);
        }

        Analyzer analyzer = new IKAnalyzer();

        Directory directory = FSDirectory.open(new File("D:\\lucene\\index"));

        IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);

        IndexWriter indexWriter = new IndexWriter(directory,writerConfig);

        for (Document document : documents) {
            indexWriter.addDocument(document);
        }
        indexWriter.close();
        return "build index success!";
    }
}
