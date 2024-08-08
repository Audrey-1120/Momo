// 전역변수
var personalTag = $('#personal-tag');
var teamTag = $('#team-tag');


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
            //const ulteam = document.getElementById('team-point');

            tagList.forEach(tag => {
                let li = document.createElement('li'); 
                li.textContent = tag.tagName; 
                li.setAttribute('data-tag-no', tag.tagNo);
                ulperson.appendChild(li); //
               // ulteam.appendChild(li); //
            });		
		 })
		  .catch(error => {
            console.error('Fetch 오류:', error);
        })
     };

// 모달에서 태그 선택시 변수에 저장해 놓음
var personChoice = [];
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

// 변수에 저장해놓은 태그들을 div box에 추가함
  $(document).on('click', '#person-choice-btn', function() {
    const $personTagUl = $('#personal-tag'); 
    $personTagUl.empty();
    personChoice.forEach(i => {
        let li = $('<li>').text(i.tagName).attr('data-tag-no', i.tagNo); 
        $personTagUl.append(li); 
        console.log('클릭');
    });
        $('#personmodal').modal('hide');
});
     

     
  







// 카테고리 불러오는 fetch


// 함수 선언부
fnGetTagList();   