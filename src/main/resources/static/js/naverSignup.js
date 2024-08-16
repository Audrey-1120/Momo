// 전역변수
var personalTag = $('#personal-tag');
let nickNameCheck = false;
let tagCheck = false;

// 헤더 부분 색깔 변경
const fnPageHeader = () => {
  const pageHeader = $('.page-header');
  pageHeader.css('backgroundImage', 'none');
  pageHeader.css('background-color', '#81C408');
}

// ------------------------- 태그 --------------------------
// 태그 불러오는 fetch
const fnGetTagList = () => {
	fetch('/getTagList.do')
	     .then(response => {
            if (!response.ok) {
                throw new Error('fetch가 돌고 있긴 함. 로그 확인');
            }
            return response.json(); 
        })
	     .then(resData => {
			//여기서부터 받아온 태그들 화면에 표시할것임
			console.log(resData);
            const tagList = resData.tagList; 
            const ulperson = document.getElementById('personal-point');

            tagList.forEach(tag => {
                let person = document.createElement('li'); 
                person.textContent = tag.tagName; 
                person.setAttribute('data-tag-no', tag.tagNo);
                ulperson.appendChild(person); 
            });
		 })
		  .catch(error => {
            console.error('Fetch 오류:', error);
        })
     };

// 모달에서 태그 선택시 변수에 저장해 놓음
var personChoice = [];
var teamChoice= [];
 $('#personal-point').on('click', 'li', function() {
	// 관건 -> no 나 name 둘 중 하나가 아니라 두개 모두 계속 같이 가야함. 화면에 표시될떄 name 데이터는 no
    const tagNo = $(this).data('tag-no'); // data-tag-no 값 가져오기
    const tagName = $(this).text();
    const index = personChoice.findIndex(item => item.tagNo === tagNo && item.tagName === tagName);
    // indexOf 배열순서 구하는 메소드 아님!! 배열에 있는지 없는지 확인하는 메소드임!!!
    if (index > -1) {
        // 배열에 존재하면 삭제
        personChoice.splice(index, 1);
        $(this).removeClass('selected');
    } else {
        // 배열에 존재하지 않으면 추가
        personChoice.push({tagNo, tagName});
        $(this).addClass('selected');
    }
    return;
});

// TagNo를 서버로 넘길때 합쳐서 넘길것이다.
const fnAttachTagNoList = () => {
	// li data 번호 가져와서 합쳐준다.
	let liTagNoListBefore = $('#personal-tag').find('li');
	let liTagNoList = liTagNoListBefore.toArray();
	let TagNoList1 = [];
	
	liTagNoList.forEach((tag) => {
	  TagNoList1.push($(tag).data('tag-no'));
	})
	
	let TagNoList = TagNoList1.join(",");
	$('.member-tagNo').val(TagNoList);
}
            
// 변수에 저장해놓은 태그들을 div에 추가함
  $(document).on('click', '#person-choice-btn', function() {
    const $personTagUl = $('#personal-tag'); 
    $personTagUl.empty();
    personChoice.forEach(i => {
        let li = $('<li>').text(i.tagName).attr('data-tag-no', i.tagNo);
        $personTagUl.append(li); 
        console.log('클릭');
    });
        $('#personmodal').modal('hide');
		fnAttachTagNoList();
		fnCheckTag();
  });
  
  $(document).on('click', '.person-notChoice-btn', function() {
	fnCheckTag();
  })

// ------------------------- 닉네임 --------------------------

// 닉네임이 10자 이하이고 공백인지?
const fnCheckNickName = () => {
  let nickName = $('.nickname-input');
  let trimmedNickName = nickName.val().trim();
  let message = $('.nickname-notice');
  if(trimmedNickName === '') {
	message.css('color', 'red');
    message.text('닉네임을 입력해주세요😭');
    nickName.focus();
    nickNameCheck = false;
    return;
  } else if(trimmedNickName.length > 10) {
	message.css('color', 'red');
    message.text('닉네임은 10자로 작성해주세요😭');
    nickNameCheck = false;
    nickName.focus();
    return;
  } else {
	message.css('color', 'yellowgreen');
	message.text('올바른 닉네임입니다🤗');
	nickNameCheck = true;
  }
}

// ------------------------- 프로필 사진 --------------------------
// 파일의 사이즈가 limit을 넘는가?
const fnIsOverSize = (file) => {
  const limit = 1024 * 1024 * 5;
  return file.size > limit;
}

// 파일 타입이 이미지 타입인가?
const fnIsImage = (file) => {
  const contentType = file.type;
  return contentType.startsWith('image');
}

// 파일 검사
const fnCheckFile = (inp) => {
	
  if(fnIsOverSize(inp.files[0])) {
	alert('첨부 파일의 최대 크기는 2MB입니다.');
	inp.value = '';
	// 파일의 경우, value가 첨부된 파일임!
	return;
  }
  
  if(!fnIsImage(inp.files[0])) {
	alert('이미지 파일(jpg, png)만 첨부할 수 있습니다😭');
	inp.value = '';
	return;
  }
  preview(inp.files[0]);
}

// 프로필 사진 미리보기
const preview = (file) => {
  const fileReader = new FileReader();
  if(file) {
    fileReader.readAsDataURL(file);
  }
  fileReader.addEventListener('load', (evt) => {
	$('#profileImage').attr('src', evt.target.result);
  })
}

// ------------------------- 태그 선택 --------------------------
// 1.personal-tag 안에 요소 가져오기(자식요소)
// 2. 없으면?

const fnCheckTag = () => {
  let personalTag = $('#personal-tag');
  let message = $('.tag-notice');
  
  if(personalTag.find('li').length === 0) {
	message.css('color', 'red');
	message.text('태그를 선택해주세요 😅');
	tagCheck = false;
	return;
  } else if (personalTag.find('li').length > 4) {
	message.css('color', 'red');
	message.text('태그는 최대 4개까지 선택할 수 있어요 😢');
	tagCheck = false;
	return;
  } else {
	message.css('color', 'yellowgreen');
	message.text('태그 선택 완료! 😢');
	tagCheck = true;
  }
}

// 함수 다 완성되면 한꺼번에 체크하기
const fnNaverSignup = () => {
  $('#frm-naverSignup').on('submit', (evt) => {
	
	fnCheckNickName();
	fnCheckTag();
	
	console.log('회원가입 바로 전 nickNameCheck: ', nickNameCheck);
	console.log('회원가입 바로 전 tagCheck: ', tagCheck);
	
	if(!nickNameCheck) {
	  alert('닉네임을 확인해주세요!');
	  evt.preventDefault();
	  $('.nickname-input').focus();
	  return;		
	} else if(!tagCheck) {
	  alert('태그를 확인해주세요!');
	  evt.preventDefault();
	  return;
	}	
	
	// 프로필 이미지 기본인지 변경했는지 체크
	let profileImageInput = $('#profileImageInput');
	if(!profileImageInput[0].files.length === 0) {
	  evt.preventDefault();
	  profileImageInput.attr('name', '');
	}
		
  });
}

$('#profileImageInput').on('change', (evt) => {
  fnCheckFile(evt.currentTarget);
})
$('.nickname-input').on('keyup', fnCheckNickName);

// 함수 선언부
fnPageHeader();
fnGetTagList();  
fnNaverSignup();
fnCheckNickName();
