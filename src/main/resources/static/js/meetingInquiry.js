

// 헤더 부분 색깔 변경
const fnPageHeader = () => {
  const pageHeader = $('.page-header');
  pageHeader.css('backgroundImage', 'none');
  pageHeader.css('background-color', '#81C408');
}

// 대분류 카테고리 클릭 시 
const fnCategoryClick = () => {
  $(document).on('click', '.major', function() {
    let middleCategory = $(this).nextAll('ul');
    middleCategory.slideToggle();
  })
  
  $(document).on('click', '.middle', function() {
    let smallCategory = $(this).nextAll('ul');
    smallCategory.slideToggle();
  })
}

// 카테고리 데이터 가져오기
const fnGetCategory = () => {
  
  fetch('/getCategory.do')
  .then((response) => response.json())
  .then(resData => {
	
	console.log(resData);
	
	let categoryList = resData.categoryList;
	
	// 대분류, 중분류, 소분류 리스트 나누기
	let majorCategory = [];
	let middleCategory = [];
	let smallCategory = [];
	
	categoryList.forEach(function(category, i) {
	  if(category.topCate === 0){
		majorCategory.push(category);
	  } else if(category.topCate === 1) {
		middleCategory.push(category);
	  } else {
		smallCategory.push(category);
	  }
	});
	
	// 1. 대분류 추가하기
	let mainCategory = $('.main-category');
	let majorCateNoList = [];
	
	majorCategory.forEach(function(category) {
	  majorCateNoList.push(category.cateNo);
	  let msg = '<li>';
	  msg += '<div class="d-flex justify-content-between fruite-name major-category-' + category.cateNo + ' major">';
	  msg += '<a href="#" onclick="return false;" data-cate-no="' + category.cateNo + '"><i class="fas fa-apple-alt me-2"></i>' + category.cateName + '</a>';
	  msg += '<span>(3)</span>';
	  msg += '</div>';
	  msg += '</li>';
	  mainCategory.append(msg);
	})
	
	// 2. 중분류 추가하기
	// 중분류를 돌면서 클래스가 major-category-번호 인 div 요소의 형제 요소(after)로 중분류 요소 추가
	middleCategory.forEach(function(category) {
		
	  let msg = '<ul class="fruite-categorie middle-category" style="display: none;">';
	  msg += '<li>';
	  msg += '<div class="d-flex justify-content-between fruite-name middle-category-' + category.cateNo + ' middle">';
	  msg += '<a href="#" onclick="return false;" data-cate-no="' + category.cateNo + '"><i class="fas fa-apple-alt me-2"></i>' + category.cateName + '</a>';
	  msg += '<span>(3)</span>';
	  msg += '</div>';
	  msg += '<ul class="fruite-categorie small-category-' + category.cateNo + '" style="display: none;"></ul>';
	  msg += '</li>';
	  msg += '</ul>';
	  let parent = '.major-category-' + category.parentNo;
	  $(parent).after(msg);
	})
	
	// 3. 소분류 가져오기
	// 중분류를 돌면서 클래스가 middle-category-번호 인 div 요소의 형제 요소인 ul 요소 안에 소분류 요소 추가
	smallCategory.forEach(function(category) {
	  let msg = '<li>';
	  msg += '<div class="d-flex justify-content-between fruite-name">';
	  msg += '<a href="#" onclick="return false;" data-cate-no="' + category.cateNo + '"><i class="fas fa-apple-alt me-2"></i>' + category.cateName + '</a>'
	  msg += '<span>(3)</span>';
	  msg += '</div>';
	  msg += '</li>';
	  let parent = '.small-category-' + category.parentNo;
	  $(parent).append(msg);
	})
  })
  .catch((error) => {
	console.log(error);
  })
}







fnPageHeader();
fnCategoryClick();
fnGetCategory();