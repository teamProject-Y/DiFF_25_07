package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.ArticleService;
import com.example.demo.service.CommentService;
import com.example.demo.service.LikeService;
import com.example.demo.vo.Article;
import com.example.demo.vo.Comment;
import com.example.demo.vo.ResultData;
import com.example.demo.vo.Rq;

@RestController
@RequestMapping("/usr/article")
public class ArticleApiController {

	@Autowired private Rq rq;
	@Autowired private ArticleService articleService;
	@Autowired private LikeService likeService;
	@Autowired private CommentService commentService;

	/** 게시글 목록 조회 */
	@GetMapping("/list")
	public ResponseEntity<?> list(
			@RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "0") int boardId,
			@RequestParam(defaultValue = "1") int searchItem,
			@RequestParam(defaultValue = "1") int page) {
		int itemsPerPage = 10;
		int offset = (page - 1) * itemsPerPage;

		int totalCnt = articleService.getArticlesCnt(keyword, boardId, searchItem);
		int totalPage = (int) Math.ceil(totalCnt / (double) itemsPerPage);
		List<Article> articles = articleService.getArticles(keyword, boardId, searchItem, offset, itemsPerPage);

		Map<String, Object> result = new HashMap<>();
		result.put("articles", articles);
		result.put("totalCnt", totalCnt);
		result.put("totalPage", totalPage);
		result.put("page", page);
		result.put("boardId", boardId);
		result.put("searchItem", searchItem);
		result.put("keyword", keyword);

		return ResponseEntity.ok(result);
	}

	/** 게시글 상세 + 댓글 */
	@GetMapping("/{id}")
	public ResponseEntity<?> detail(@PathVariable int id) {
		long memberId = rq.getLoginedMemberId();
		Article article = articleService.getArticleForPrint(id, (int) memberId);
		if (article == null) {
			return ResponseEntity.notFound().build();
		}
		List<Comment> comments = commentService.getComments(id, (int) memberId);
		Map<String, Object> result = new HashMap<>();
		result.put("article", article);
		result.put("comments", comments);
		return ResponseEntity.ok(result);
	}

	/** 좋아요 토글 */
	@PostMapping("/{id}/like")
	public ResponseEntity<ResultData> toggleLike(@PathVariable int id) {
		int memberId = (int) rq.getLoginedMemberId();
		if (memberId <= 0) {
			return ResponseEntity.status(401)
					.body(ResultData.from("F-1", "로그인 후 이용해주세요"));
		}

		// 기존 좋아요 여부 체크
		boolean isLiked = likeService.isMyLike(memberId, id);
		ResultData rd;
		if (isLiked) {
			// 좋아요 취소
			rd = likeService.deleteLike(memberId, id);
		} else {
			// 좋아요 추가
			rd = likeService.insertLike(memberId, id);
		}

		// 새로운 좋아요 개수 생성하여 반환
		int likeCount = likeService.getLikes(id);
		return ResponseEntity.ok(
				ResultData.from(
						rd.getResultCode(),
						rd.getMsg(),
						"likeCount", likeCount
				)
		);
	}

	/** 조회수 증가 */
	@PostMapping("/{id}/hit")
	public ResponseEntity<ResultData> incHits(@PathVariable int id) {
		ResultData rd = articleService.doIncHits(id);
		return ResponseEntity.ok(rd);
	}

	/** 댓글 작성 */
	@PostMapping("/{id}/comment")
	public ResponseEntity<ResultData> writeComment(
			@PathVariable int id,
			@RequestBody Map<String, String> bodyMap) {
		int memberId = (int) rq.getLoginedMemberId();
		String body = bodyMap.get("body");
		if (body == null || body.isBlank()) {
			return ResponseEntity.badRequest()
					.body(ResultData.from("F-1", "내용을 입력해주세요"));
		}
		commentService.doCommentWrite("article", id, memberId, body);
		return ResponseEntity.ok(ResultData.from("S-1", "댓글 작성 완료"));
	}

	/** 게시글 작성 */
	@PostMapping
	public ResponseEntity<ResultData> write(@RequestBody Article dto) {
		int memberId = (int) rq.getLoginedMemberId();
		if (memberId <= 0) {
			return ResponseEntity.status(401)
					.body(ResultData.from("F-1", "로그인 후 이용해주세요"));
		}
		if (dto.getBoardId() == 0) {
			return ResponseEntity.badRequest()
					.body(ResultData.from("F-2", "게시판을 선택해주세요"));
		}
		articleService.writeArticle(dto.getTitle(), dto.getBody(), memberId, dto.getBoardId());
		return ResponseEntity.ok(ResultData.from("S-1", "게시글 작성 완료"));
	}

	/** 수정 */
	@PutMapping("/{id}/modify")
	public ResponseEntity<ResultData> modify(
			@PathVariable int id,
			@RequestBody Article dto) {
		int memberId = (int) rq.getLoginedMemberId();
		Article article = articleService.getArticleById(id);
		if (article == null) {
			return ResponseEntity.notFound().build();
		}
		if (article.getMemberId() != memberId) {
			return ResponseEntity.status(403)
					.body(ResultData.from("F-1", "권한이 없습니다"));
		}
		articleService.modifyArticle(id, dto.getTitle(), dto.getBody());
		return ResponseEntity.ok(ResultData.from("S-1", "게시글 수정 완료"));
	}

	/** 삭제 */
	@DeleteMapping("/{id}/delete")
	public ResponseEntity<ResultData> delete(@PathVariable int id) {
		int memberId = (int) rq.getLoginedMemberId();
		Article article = articleService.getArticleById(id);
		if (article == null) {
			return ResponseEntity.notFound().build();
		}
		if (article.getMemberId() != memberId) {
			return ResponseEntity.status(403)
					.body(ResultData.from("F-1", "권한이 없습니다"));
		}
		articleService.deleteArticle(id);
		return ResponseEntity.ok(ResultData.from("S-1", "게시글 삭제 완료"));
	}
}