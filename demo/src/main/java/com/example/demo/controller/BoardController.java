package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.domain.Post;
import com.example.demo.model.dto.PostForm;
import com.example.demo.model.service.CommentService;
import com.example.demo.model.service.PostService;

@Controller // 게시판 화면 컨트롤러
@RequestMapping("/board") // 모든 URL 앞에 /board
public class BoardController {
    @Autowired
    PostService postService;
    
    @Autowired // ① 필드 추가 (postService 아래)
    CommentService commentService;

    @GetMapping // 목록 : GET /board?page=0&type=titleContent&keyword=스프링 (누구나)
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "titleContent") String type, // ✅ 검색 범위 추가
                       @RequestParam(required = false) String keyword, 
                       Model model) {
        
        List<Post> posts = postService.search(type, keyword);
        model.addAttribute("posts", posts);
        model.addAttribute("type", type);       // ✅ 화면에서 선택 상자 선택 상태 유지
        model.addAttribute("keyword", keyword); // 검색어 유지
        return "board/list";
    }

    // ✅ 내가 쓴 글 목록 (페이징 포함 - 하나만 유지)
    @GetMapping("/my")
    public String myPosts(@RequestParam(defaultValue = "0") int page, Principal principal, Model model) {
        Page<Post> posts = postService.getMyPosts(principal.getName(), page);
        model.addAttribute("posts", posts);
        model.addAttribute("listTitle", "내가 쓴 글");
        model.addAttribute("pageUrl", "/board/my"); // 페이징 링크용
        return "board/list";
    }

    @GetMapping("/write") // 글쓰기 화면 : GET /board/write (로그인 필요)
    public String writeForm() {
        return "board/form";
    }

    @PostMapping("/write") // 글쓰기 처리 : POST /board/write
    public String write(PostForm postForm, Principal principal, Model model) {
        try {
            Post post = postService.write(postForm, principal.getName()); // 작성자 = 로그인한 사람
            return "redirect:/board/" + post.getId(); // 저장 후 상세 화면으로
        } catch (IllegalArgumentException e) { // 빈 제목·내용
            model.addAttribute("error", e.getMessage());
            return "board/form"; // 입력값 유지한 채 다시 글쓰기 화면
        }
    }

    @GetMapping("/{id}") // 상세 : GET /board/3 (누구나)
    public String detail(@PathVariable Long id, Model model) {
        Post post = postService.read(id);
        model.addAttribute("post", post);
        model.addAttribute("comments", commentService.findByPost(post));
        return "board/detail";
    }

    @GetMapping("/{id}/edit") // 수정 화면 : GET /board/3/edit
    public String editForm(@PathVariable Long id, Model model) {
        Post post = postService.findForEdit(id);
        PostForm postForm = new PostForm(); // 기존 내용을 폼에 채워서 보여 준다
        postForm.setTitle(post.getTitle());
        postForm.setContent(post.getContent());
        model.addAttribute("postForm", postForm);
        model.addAttribute("postId", id); // 화면이 "수정" 모드로 바뀐다
        return "board/form";
    }

    @PostMapping("/{id}/edit") // 수정 처리 : POST /board/3/edit
    public String edit(@PathVariable Long id, PostForm postForm, Model model) {
        try {
            postService.edit(id, postForm);
            return "redirect:/board/" + id;
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("postId", id);
            return "board/form";
        }
    }

    @PostMapping("/{id}/delete") // 삭제 처리 : POST /board/3/delete
    public String delete(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/board?deleted"; // 목록 + "삭제되었습니다"
    }
}