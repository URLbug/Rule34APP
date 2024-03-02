package com.example.rule34

class PostsModel(
    var preview_url: String,
    var sample_url: String,
    var file_url: String,
    var directory: Int,
    var hash: String,
    var width: Int,
    var height: Int,
    var image: String,
    var change: Int,
    var owner: String,
    var parent_id: Int,
    var rating: String,
    var sample: Boolean,
    var sample_height: Int,
    var sample_width: Int,
    var score: Int,
    var tags: String,
    var source: String,
    var status: String,
    var has_notes: Boolean,
    var comment_count: Int
) {
}