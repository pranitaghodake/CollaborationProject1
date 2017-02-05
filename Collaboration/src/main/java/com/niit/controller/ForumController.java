package com.niit.controller;

import java.lang.annotation.Repeatable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.model.Categories;
import com.niit.model.CommentModel;
import com.niit.model.ForumBasicModel;
import com.niit.model.ForumModel;
import com.niit.model.ForumViewModel;
import com.niit.model.Forums;
import com.niit.model.PostModel;
import com.niit.model.Posts;
import com.niit.model.UserProfiles;
import com.niit.services.CategoryService;
import com.niit.services.ForumService;
import com.niit.services.PostService;
import com.niit.services.UserProfileService;

@RestController
public class ForumController {

	@Autowired
	private ForumService forumService;

	@Autowired
	private CategoryService categoryService;
	
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private UserProfileService userProfileService;

	@RequestMapping(value = "admin/forums/admin/forum/all")
	public @ResponseBody List<ForumModel> getForums() {
		// Create a list of forum model
		List<ForumModel> list = new ArrayList<ForumModel>();
		List<Forums> forums = null;
		List<Categories> categories = this.categoryService.getAll();
		ForumModel model = null;
		for(Categories category: categories) {
			forums = this.forumService.getForumsByCategory(category.getId());
			model = new ForumModel();
			model.setCategory(category);
			model.setForums(forums);
			list.add(model);
		}
		
		return list;		
	}
	@RequestMapping(value="admin/forums/admin/remove/{forumId}")		
	public void deleteForum(@PathVariable("forumId") String forumId){
		Forums forum = this.forumService.get(forumId);
		forumService.remove(forum);
	}
	
	@RequestMapping(value = "public/forums/all")
	
	public @ResponseBody List<ForumBasicModel> getPublicForums() {
		
		List<ForumBasicModel> listForumBasicModel = new ArrayList<ForumBasicModel>();
		// get all the categories first
		List<Categories> categories = this.categoryService.getAll();
		
		ForumBasicModel forumBasicModel = null;
		List<Forums> forumList = null;
		
		for(Categories category: categories) {
			// fetch all the forums based on categories
			forumList = this.forumService.getForumsByCategory(category.getId());
			
			if(forumList!=null) {				
				for(Forums forum: forumList) {
					forumBasicModel = new ForumBasicModel(category, forum);
					listForumBasicModel.add(forumBasicModel);
				}				
			}
			
			
		}
		
		return listForumBasicModel;		
	}
	@RequestMapping(value = "public/view/forum/get/forum/{forumId}")
	public @ResponseBody ForumViewModel getForumView(@PathVariable("forumId") String forumId) {		
		Forums     forum= this.forumService.get(forumId);
		
		ForumViewModel forumViewModel = new ForumViewModel(forum);
		
		PostModel postModel;
		UserProfiles profile;
		// Get all the posts
		List<Posts> posts = this.postService.getPostsByForum(forumId);
		if(posts!=null) {
			for(Posts post: posts) {
				profile = this.userProfileService.get(post.getUserId());					
				postModel = new PostModel(post, profile);
				forumViewModel.add(postModel);
			}
		}
		 				
		return forumViewModel;
	}
	
	
}
