

  
  const pageHeader = $('.page-header');
  pageHeader.css('backgroundImage', 'none');
  pageHeader.css('background-color', '#81C408');
  
  // 카테고리 - 카테고리 데이터(임시로 하드코딩)
  const categories = [
	{CATE_NO: 1, TOP_CATE: 0, PARENT_NO: null, CATE_NAME: "문화&예술"},
	{CATE_NO: 2, TOP_CATE: 1, PARENT_NO: 1, CATE_NAME: "영화"},
	{CATE_NO: 3, TOP_CATE: 2, PARENT_NO: 2, CATE_NAME: "공포"},
	{CATE_NO: 4, TOP_CATE: 2, PARENT_NO: 2, CATE_NAME: "로맨스"},
	{CATE_NO: 5, TOP_CATE: 2, PARENT_NO: 2, CATE_NAME: "액션"},
	{CATE_NO: 6, TOP_CATE: 1, PARENT_NO: 1, CATE_NAME: "음악"},
	{CATE_NO: 7, TOP_CATE: 2, PARENT_NO: 6, CATE_NAME: "클래식"},
	{CATE_NO: 8, TOP_CATE: 2, PARENT_NO: 6, CATE_NAME: "록"},
	{CATE_NO: 9, TOP_CATE: 0, PARENT_NO: null, CATE_NAME: "취미&오락"},
	{CATE_NO: 10, TOP_CATE: 1, PARENT_NO: 9, CATE_NAME: "게임"},
	{CATE_NO: 11, TOP_CATE: 2, PARENT_NO: 10, CATE_NAME: "카드게임"},
	{CATE_NO: 12, TOP_CATE: 2, PARENT_NO: 10, CATE_NAME: "보드게임"}
  ];
  
  // 트리 구조로 변환하는 함수
  const fnBuildTree = (categories) => {
	const jstreeData = [];
	
	// 대분류 root node로 설정
	categories.forEach(category => {
	  if(category.TOP_CATE === 0) {
		jstreeData.push({
		  id: category.CATE_NO,
		  parent: '#',
		  text: category.CATE_NAME,
		  icon: "fa fa-ghost"
		})
	  } else if(category.TOP_CATE === 1) {
		jstreeData.push({
		  id: category.CATE_NO,
		  parent: category.PARENT_NO,
		  text: category.CATE_NAME,
		  icon: "fa fa-ghost"
		})
	  } else {
		jstreeData.push({
		  id: category.CATE_NO,
		  parent: category.PARENT_NO,
		  text: category.CATE_NAME,
		  icon: "fa fa-hippo"
		})
	  }
	})
	
	console.log('jstreeData', jstreeData);
	
    $('#category-list').jstree({
	  'core': {
		  'data': jstreeData,
        'themes': {
           'icons': true
        }
	  },
	  'plugins': ['search', 'checkbox'],
	    'checkbox': {
	       'keep_selected_style': true,
	       'three_state': false,
	       'whole_node' : false,
	       'tie_selection' : false,
	       'cascade': 'down'
	    }      		  
    }).on('ready.jstree', function() {
	  $(this).jstree(true).open_all();
    })
	
	
  }

  // 카테고리 버튼 클릭 시 추가 혹은 수정 화면 띄워주기
  const fnShowCategoryEdit = () => {
	let addCategorySection = $('.add-category-sec');
	let modifyCategorySection = $('.modify-category-sec');
	
	// 1. 추가 버튼 클릭 시
	$('.btn-addCate').on('click', () => {
	  addCategorySection.toggle();
	  modifyCategorySection.hide();
	})
	
	// 2. 수정 버튼 클릭 시
	$('.btn-modifyCate').on('click', () => {
	  addCategorySection.hide();
	  modifyCategorySection.toggle();
	})
  }
  
  // 태그 버튼 추가 혹은 수정 화면 표시
  const fnShowTagEdit = () => {
	let addTagSection = $('.add-tag-sec');
	let modifyTagSection = $('.modify-tag-sec');
	let modifyTagInput = $('#modifyTagInput');
	
	// 1. 추가 버튼 클릭 시
	$('.btn-addTag').on('click', () => {
	  addTagSection.toggle();
	  modifyTagSection.hide();
	})
	
	// 2. 수정 버튼 클릭 시
	$('.modify-tag').on('click', (evt) => {
      console.log('태그 수정 버튼 클릭');
		
	  addTagSection.hide();
	  modifyTagSection.show();
	  
	  // 클릭한 요소의 형제 요소의 태그명 가져와서 input에 넣기
	  
	  let tagName = $(evt.currentTarget).closest('tr').find('td:nth-child(2)').text().trim();
	  
	  modifyTagInput.val(tagName);
	  
	})
  }






  // 선택한 분류에 따라서 카테고리 추가
  const fnAddCategory = () => {
	
	let selectedCategory = $('#selectCategory');
	let majorCate = $('.majorCate');
	let middleCate = $('.middleCate');
	let smallCate = $('.smallCate');
	
	selectedCategory.on('change', () => {
	  if(selectedCategory.val() === 'majorCate') {
	    majorCate.toggle();
	    middleCate.hide();
	    smallCate.hide();
	  } else if(selectedCategory.val() === 'middleCate') {
	    majorCate.hide();
	    middleCate.toggle();
	    smallCate.hide();
	  } else {
	    majorCate.hide();
	    middleCate.hide();
	    smallCate.toggle();
	  }
	})
  }
  
  fnBuildTree(categories);
  fnAddCategory();
  fnShowCategoryEdit();
  fnShowTagEdit();
  
  