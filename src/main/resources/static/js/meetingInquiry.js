// --------- 전역변수 ---------
// 카테번호
let globalCateNo = 0;
let globalSort = 'createdDate,DESC';


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
	  msg += '<a href="#" onclick="return false;" data-cate-no="' + category.cateNo + '"><i class="fas fa-kiwi-bird me-2"></i>' + category.cateName + '</a>';
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
	  msg += '<a href="#" onclick="return false;" data-cate-no="' + category.cateNo + '"><i class="fas fa-kiwi-bird me-2"></i>' + category.cateName + '</a>';
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
	  msg += '<a href="#" onclick="fnGetMeetingList(' + 0  + ',\'' + globalSort + '\',' + category.cateNo +')" class="cateElement" data-cate-no="' + category.cateNo + '"><i class="fas fa-kiwi-bird me-2"></i>' + category.cateName + '</a>'
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

// 미팅 데이터 가져오기
const fnGetMeetingList = (page, sort, cateNo) => {
  
  globalCateNo = cateNo;
  
  // 데이터 없을 경우
  let path = '';
  if(sort === 'null') {
	path = '/getMeetingList.do?page=' + page + '&size=' + 9 + '&sort=' + encodeURIComponent(globalSort) + '&cateNo=' + cateNo;
  } else {
	path = '/getMeetingList.do?page=' + page + '&size=' + 9 + '&sort=' + sort + '&cateNo=' + cateNo;
  }
  
  fetch(path)
  	.then(response => response.json())
  	.then(resData => {
		
	  console.log(resData);

	  // 모임 데이터 추가할 부분 및 모임 데이터	  
	  let meetingContainer = $('.meeting-form');
	  let meetingList = resData.meetingDtoPage.contents;
	  
	  if(meetingList.length === 0) {
		let msg = '<p style="font-size: 20px; text-align: center;" class="mt-2 none-meeting">아직 만들어진 모임이 없어요 🥹</p>'
		$('.none-meeting').remove();
		$('.meetings').remove();
		$('.pagination-form').remove();
		meetingContainer.append(msg);
		return;
	  }
	  
	  
	  // 태그 데이터
	  let tagDataList = resData.tagList;
	  
	  // 카테고리 데이터
	  let cateDataList = resData.cateList;
	  
	  // 페이지네이션을 위한 페이지들
	  let currentPage = resData.meetingDtoPage.currentPage;
	  let totalPage = resData.meetingDtoPage.totalPage;
	  let beginPage = resData.meetingDtoPage.beginPage;
	  let endPage = resData.meetingDtoPage.endPage;
	  
	  // 태그 데이터를 돌면서 key값은 tagNo, value값은 tagName으로 된 새 Map 생성
	  let tagList = new Map();
	  tagDataList.forEach((tag) => {
		tagList.set(tag.tagNo, tag.tagName);
	  })
	  
	  // 카테고리 데이터를 돌면서 key값은 cateNo, value값은 cateName으로 된 새 Map 생성
	  let cateList = new Map();
	  cateDataList.forEach((cate) => {
		cateList.set(cate.cateNo, cate.cateName);
	  })
	  
	  // 화면 비워주기
	  $('.none-meeting').remove();
	  $('.meetings').remove();
	  
	  // 모임 추가하기
	  meetingList.forEach(function(meeting) {
		
		// 태그 번호 리스트
		let meetingTag = meeting.tagNo.split(",").map(Number);
		
		let msg = '<div class="col-md-6 col-lg-6 col-xl-4 meetings" data-meeting-no="' + meeting.meetingNo + '">';
		msg += '<div class="rounded position-relative fruite-item" style="height:auto;">';
		msg += '<div class="fruite-img">';
		msg += '<img src="img/등산.jpg" class="img-fluid w-100 rounded-top" alt="" style="height: 205px;">';
		msg += '</div>';
		msg += '<div class="text-white bg-secondary px-3 py-1 rounded position-absolute" style="top: 10px; left: 10px;">' + cateList.get(meeting.cateNo) + '</div>';	
		msg += '<div class="p-4 border border-secondary border-top-0 rounded-bottom">';
		msg += '<h4>' + meeting.meetingTitle + '</h4>';
		msg += '<p class="m-auto">현재 인원 수: <span style="color: orange;">' + meeting.participantsCount + '</span>/' + meeting.capacity + '</p>';
		msg += '<div class="form-control my-2 tag-form" style="background-color: white;">';
		msg += '<ul id="personal-tag" class="ps-0">';
		meetingTag.forEach(function(tag) {
		  // 이 사이에 태그 추가해주기!!!!
		    msg += '<li>' + tagList.get(tag) + '</li>'; 
		})
		msg += '</ul>';
		msg += '</div>';
		msg += '<div class="d-flex justify-content-between flex-lg-wrap">';
		msg += '<a href="#" class="btn border border-secondary rounded-pill px-3 text-primary">가입신청</a>';
		msg += '</div>';
		msg += '</div>';
		msg += '</div>';
		msg += '</div>';
		
		meetingContainer.append(msg);
	  })
	  
	  // 페이지네이션 함수 호출
	  fnGetPaging(currentPage, totalPage, beginPage, endPage);
	})
}

// 페이지네이션
const fnGetPaging = (currentPage, totalPage, beginPage, endPage) => {
  
  // 페이징 추가할 부분 가져오기
  let pagingContainer = $('.meeting-form');
  let paging = '<div class="col-12 pagination-form">';
  paging += '<div class="pagination d-flex justify-content-center mt-5">';
  
  if(beginPage === 1) {
    paging += '<a href="javascript:void(0);" onclick="return false;" class="rounded">&laquo;</a>';
  } else {
    paging += '<a href="javascript:void(0);" onclick="fnGetMeetingList(' + (beginPage - 1) + ',\'' + globalSort + '\',' + globalCateNo + ')" class="rounded">&laquo;</a>';
  }
  
  for(let i = beginPage; i <= endPage; i++) {
	if(i === currentPage) {
	  paging += '<a href="javascript:void(0);" onclick="fnGetMeetingList(' + i + ',\'' + globalSort + '\',' + globalCateNo + ')" class="active rounded">' + i + '</a>';
	} else {
	  paging += '<a href="javascript:void(0);" onclick="fnGetMeetingList(' + i + ',\'' + globalSort + '\',' + globalCateNo + ')" class="rounded">' + i + '</a>';
	}
  }
  
  if(endPage === totalPage) {
	paging += '<a href="javascript:void(0);" onclick="return false;" class="rounded">&raquo;</a>';
  } else {
	paging += '<a href="javascript:void(0);" onclick="fnGetMeetingList(' + (endPage + 1) + ',\'' + globalSort + '\',' + globalCateNo + ')" class="rounded">&raquo;</a>';
  }
  
  paging += '</div>';
  paging += '</div>';
  
  // 페이징 추가하기 전에 원래 요소 삭제
  $('.pagination-form').remove();
  
  // 패이징 설정
  pagingContainer.append(paging);
  
}

// select 박스에서 오래된순, 최신순 조회시
const fnSortMeeting = () => {
  // 선택된 요소의 val값
  let selectedSort = $('#fruits option:selected').val();
  globalSort = selectedSort;
  fnGetMeetingList(1, selectedSort, globalCateNo);
}

// 카테고리 클릭 시 제목 변경
const fnModifytitle = () => {
  $(document).on('click', '.cateElement', function() {
	$('.category-title').text($(this).text());
  })
}



fnPageHeader();
fnCategoryClick();
fnGetCategory();
fnGetMeetingList(1, 'createdDate,DESC', globalCateNo);
fnModifytitle();