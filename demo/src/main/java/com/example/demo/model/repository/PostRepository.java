/*package com.example.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.domain.Post;

@Repository // 게시글 리포지토리 : save, findAll, findById, delete 는 이미 있다
public interface PostRepository extends JpaRepository<Post, Long> {
}*/
package com.example.demo.model.repository;

import java.util.List; // ✅ 추가
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.domain.Post;

@Repository // 게시글 리포지토리 : save, findAll, findById, delete 는 이미 있다
public interface PostRepository extends JpaRepository<Post, Long> {

    // ✅ 추가: 작성자 username 기준 조회 + 최신 글이 위로 (findAll과 동일하게 id 내림차순)
    List<Post> findByAuthor_UsernameOrderByIdDesc(String username);
}
