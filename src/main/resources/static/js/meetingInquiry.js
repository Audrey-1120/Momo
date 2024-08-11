

// 헤더 부분 색깔 변경
const fnPageHeader = () => {
  const pageHeader = $('.page-header');
  pageHeader.css('backgroundImage', 'none');
  pageHeader.css('background-color', '#81C408');
}

// 대분류 카테고리 클릭 시 

/*
$('.major-category').on('click', () =>{
  // 1. 중분류 카테고리(middle-category의 display값 block으로 변경)
  $('.middle-category').removeClass('disappear');
  $('.middle-category').addClass('appear');
})

$('.middle-category').on('click', function() {
	const target = this;
	if(target.hasClass('appear')) {
	  target.addClass('disapper');
	  setTimeout(function(){target.removeClass('appear')},1001);
	}
})

*/

$('.major-category').click(function() {
  let middleCategory = $(this).nextAll('ul');
  if(middleCategory.is(':visible')) {
	middleCategory.slideUp();
  } else {
	middleCategory.slideDown();
  }
})

$('.middle-item').click(function() {
  let smallCategory = $(this).nextAll('ul');
  if(smallCategory.is(':visible')) {
	smallCategory.slideUp();
  } else {
	smallCategory.slideDown();
  }
})




fnPageHeader();