# 프로젝트 목표
<ul>
  <li>JDBC를 사용한 DB 연동 기술</li>
  <li>Swing을 사용한 Java Application 제작</li>
  <li>팀원 별로 패키지 단위로 나눠 제작 후 병합하는 방법 숙달</li>
</ul>

# 초기 화면
<img src="https://postfiles.pstatic.net/MjAxOTA1MDhfMTY3/MDAxNTU3MjkwNTIwMDI4.sOZeD-C85FjbCqvcQnZsGpXcgv5EBLpKowzQgFhqqq8g.n6x9ghRUeivEe4U0cRqbzgyp41HJwzgHWdOYQq9c7uUg.PNG.younggu1545/%EA%B7%B8%EB%A6%BC22.png?type=w966"/>
<ul>
  <li>로그인 시 로그인 화면 표시</li>
  <li>등록 버튼 누를 시 회원 가입할 수 있는 창 표시</li>
  <li>SHA-256방식으로 암호화 하여 DB에 저장하는 기능 구현</li>
</ul>

# 달력 화면
<img src="https://postfiles.pstatic.net/MjAxOTA1MDhfMTEz/MDAxNTU3MjkwNzk4NTgw.xEI4ic2i_njWaYg0uGmK3LnbXIhkipWOzauBNXr74FQg.thk3Nnt43MAiw5WOnhBJ9VL_98VgDEPkuQNkWv6B5xYg.PNG.younggu1545/%EA%B7%B8%EB%A6%BC23.png?type=w966"/>
<ul>
  <li>로그인 후 보여지는 초기 화면</li>
  <li>상단 타이틀에 현재 접속한 계정 표시</li>
  <li>메뉴바 사용하여 각 메뉴별로 이동</li>
  <li>세부 일정보기
      <ol>
        <li>각 일정 누르면 세부 일정 볼 수 있는 화면으로 전환</li>
        <li>일정별로 고유의 색상을 부여하여 각 일정을 구별하기 쉽게 표현</li>
        <li>표시할 수 있는 한계를 넘어서면 숫자로 넘어가는 갯수 표시하고, 해당 숫자 누르면 선택할 수 있는 창 표시</li>
        <li>일정이 다음주로 넘어가면 각 주 첫 일정에 어떤 일정인지 이름 표서</li>
      </ol>
  </li>
  <li>일정등록
    <ul>
      <li>상단의 일정등록 메뉴 누르면 오늘 잘짜로 일정 등록 가능</li>
      <li>달력의 빈 공간 선택하면 해당 날짜를 기준으로 일정 등록할 수 있도록 표시</li>
    </ul>
  </li>
</ul>

# 일정 보기 및 수정
<img src="https://postfiles.pstatic.net/MjAxOTA1MDhfMTAw/MDAxNTU3MjkyMDYxNjQ5.YkP-KmuaKb2iIOUe_vV7bhEU_81bZ6LEbkNvX5KDV6Ag.8tLBCq2TyHS39jdQSKJSwFmQOKgwlqhsNglo0Uxh9L0g.PNG.younggu1545/%EA%B7%B8%EB%A6%BC24.png?type=w966"/>
<ul>
  <li>달력에서 해당 일정 누르면 일정 볼 수 있는 화면으로 넘어갈 수 있도록 구현</li>
  <li>자신이 작성한 일정은 수정 가능하도록 표시(좌측)</li>
  <li>다른사람이 작성한 글은 수정할 수 없도록 작성(우측)</li>
</ul>

# 게시판
<img src="https://postfiles.pstatic.net/MjAxOTA1MDhfMjAw/MDAxNTU3MjkyMjMwMjA2.DsAso-fQea_nZaj27kiujDpCYnB9lJw-ppB9nn-OscIg.Ia_wCqyq3aPSDObI3h-KS8yuWsGSS6AcgDBuJdcezzYg.PNG.younggu1545/%EA%B7%B8%EB%A6%BC25.png?type=w966"/>
<ul>
  <li>게시판 선택 시 현재 등록 된 게시글 볼 수 있도록 표시</li>
  <li>검색 기능 사용하여 제목, 작성자, 아이디 별로 검색 기능 구현</li>  
</ul>

# 정보 수정
<img src="https://postfiles.pstatic.net/MjAxOTA1MDhfMjM5/MDAxNTU3Mjg4OTY3Nzgw.mGVwMGRBzmSy--PAJ8v_E4I7NlIqX8cSvu1Px-XPsLYg.rj1X9m6D5WykU6epk6DMuCbC8TJpDRAW-Vzi5U5D8ssg.PNG.younggu1545/%EA%B7%B8%EB%A6%BC11.png?type=w966"/>
<ul>
  <li>아이디 및 이름은 수정 불가능 하도록 설정</li>
  <li>이메일/전화번호 변경 시 비밀번호 일치해야 변경 되도록 설정</li>
  <li>비밀번호 변경 시 기존 비밀번호와 새로 작성한 비밀번호들이 일치해야 변경 가능 하도록 작성</li>
  <li>비밀번호 일치해야 회원 탈퇴 가능하도록 작성</li>
</ul>

# 회원 관리(관리자 화면)
<img src="https://postfiles.pstatic.net/MjAxOTA1MDhfNzAg/MDAxNTU3Mjg4OTY4MDcw.HZLWLEKgTltEvrF9t61O9FvNfTS0PDGZc5xmeURnSe0g._PFGiQ4QgWbcGGY0WnsieiEOuQ21IYdHdrOiLwtzdV8g.PNG.younggu1545/%EA%B7%B8%EB%A6%BC13.png?type=w966"/>
<ul>
  <li>관리자로 설정 된 멤버만 해당 메뉴가 표시 되도록 작성</li>
  <li>아이디 및 이름은 수정 불가능하며, 이메일, 전화번호, 어드민 설정만 수정가능하며 수정 시 update 진행</li>
  <li>선택한 회원의 회원 삭제 및 비밀번호 초기화 기능 구현 </li>
  <li>전체 리스트에 대하여 엑셀로 다운 </li>
  <li>아이디, 이메일, 이름, 전화번호로 검색 기능 구현</li>
</ul>

# Tray
<img src="https://postfiles.pstatic.net/MjAxOTA1MDhfMjEw/MDAxNTU3MjkzODE0Njcz.u7fx2oj3W6YF1q24ZpnXhwKFVy6XMHrtiUQz_Kq8zKsg.p13oVMuBmPNyCpr1RyzUEubiYrbtHS8HjLBZenr11owg.PNG.younggu1545/%EA%B7%B8%EB%A6%BC26.png?type=w966"/>
<ul>
  <li>항상 켜 두어야 하는 프로그램 특성 상 최소화 버튼 클릭 시 트레이 창으로 가는 기능 구현</li>
  <li>트레이 아이콘을 더블 클릭하면 최근에 작업했던 창으로 열리도록 구현</li>
  <li>아이콘 우 클릭 시 원하는 메뉴 바로 갈 수 있는 선택 창 표시</li>
  <li>Thread 기능 활용하여 다른 사람이 일정 등록 시 알림 창 표시 할 수 있는 기능 구현</li>
</ul>


# 클래스 다이어그램
<img src="https://postfiles.pstatic.net/MjAxOTA1MDhfMjgz/MDAxNTU3Mjg4OTY4MTUw.1uSDOkAHTwnOSumhQE3AMHasSTqMbl8-4pKvu11-RBEg.7tkssHwJehr-LuULTc5qe66XHC-o1IiRr7Kxq3d8Lvcg.PNG.younggu1545/%EA%B7%B8%EB%A6%BC17.png?type=w966"/>
<img src="https://postfiles.pstatic.net/MjAxOTA1MDhfODMg/MDAxNTU3Mjg4OTY4Mzkx.j2hHwj_DzwXepTh256kfghC0WWKqsQBnTNOdrIxfH5Ig.kNj_aJfxeDauHlfR60nIEe0oqo1D9tM2EX2IghO9H74g.PNG.younggu1545/%EA%B7%B8%EB%A6%BC18.png?type=w966"/>
<img src="https://postfiles.pstatic.net/MjAxOTA1MDhfMzIg/MDAxNTU3Mjg4OTY4Mzg5.v_tiULxwpmFL_IpQZKqxBSrN1iFV9xjTIaWb-wJz_Jog.Z-22JYEv6BNZVDu8L0mqQxyuOsJdlQBTJidFVlQfA_cg.PNG.younggu1545/%EA%B7%B8%EB%A6%BC19.png?type=w966"/>
<img src="https://postfiles.pstatic.net/MjAxOTA1MDhfMjM1/MDAxNTU3Mjg4OTY4Mzcw.p87bfX_stXOj-Rs37HEFVWovwEUWhBWm8UYJs7Vftfsg.g8oK8K-fy-oiusvqzbDc0EkuBtfuQ6P9jOXy1zskyUAg.PNG.younggu1545/%EA%B7%B8%EB%A6%BC20.png?type=w966"/>
