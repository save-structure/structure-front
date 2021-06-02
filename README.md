# 0. 프로젝트 실행 방법
- YouTube Player API_KEY를 YoutubeConfig.java 파일의 API_KEY 변수에 문자열 형태로 대입
- CLOVA Face Recognition(CFR) API_KEY를 EmotionFrag.java 파일의 getAPI 클래스 내의 run() 함수의 clientSecret 변수에 문자열 형태로 대입
- CLOVA Face Recognition(CFR) ID를 EmotionFrag.java 파일의 getAPI 클래스 내의 run() 함수의 clientId 변수에 문자열 형태로 대입

- YouTube Player API를 이용한 영상재생은 안드로이드 스튜디오의 애뮬레이터가 10.0 버전이어야 하고, CLOVA Face Recognition(CFR) API를 이용한 표정 인식은 애뮬레이터가 11.0 버전이어야 해서, 두 개의 API 오류 없이 사용하기 위해서는 안드로이드 스튜디오의 애뮬레이터가 아닌 실기기를 연결해 실행시켜 보는 것을 권장함

# 1. 개발 주제
음악과 날씨, 음악과 기분의 상관 관계에 주목하여 시작된 프로젝트. 현재 날씨와 어울리는 음악 한 곡과 사용자의 기분에 어울리는 음악 한 곡, 하루에 총 두 곡을 추천해 주는 노래 추천 서비스. 오디.

# 2. 개발 내용
2.1. 내 위치를 기반으로 한 날씨 조회
2.2. 오늘의 날씨 정보를 바탕으로 음악 추천
2.3. 입력한 기분 저장 + 입력한 기분을 바탕으로 추천 음악 조회 [Youtube API 연동]
2.4. 날씨별 + 기분별 음악 정보 조회 [캘린더 팝업 화면]
2.5. 월별 기분별 통계 + 전체 기분별 통계
2.6. 플레이리스트

# 3. 소스 코드 모듈별 내용 정리
## 프론트
- 날씨 기반 추천 모듈
>WeatherFrag
class find_weather extends Thread : 현재 날씨를 서버에서 받아와 출력
void loadIcon(String icon) : 현재 날씨에 해당하는 날씨 아이콘을 받아와서 보여줌
class find_weatherbase_music extends Thread :현재 날씨를 기반으로 추천된 노래를 받아옴
void postUpData() : 해당 노래에 대한 정보를 좋아요 플레이리스트로 전송

- 기분 기반 추천 모듈
>EmotionFrag
void postEmotionData() : 감정 상태를 서버에 전송
class getEmotionData extends Thread : 감정 상태를 기반으로 추천된 노래를 조회
void postPlaylistAdd() : 한 번 누르면 추천 받은 노래를 플레이리스트에 추가, 다시 누르면 삭제
void requestCamera() : 카메라 권한 요청
File createImageFile() : 이미지를 저장할 파일 경로 미리 생성
void captureImage() : 사진 촬영
void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) 
	: requestCode가 IMAGE_REQUEST일 시 사진촬영 이후의 작업을 실행
class getAPI extends Thread : 촬영한 사진으로 표정 분석
int getfeelingId(StringBuffer res) : getAPI 로 얻은 JSONObject로부터 기분 인덱스를 반환
>EmotionSettingFrag
void postSettingData() : 사용자가 설정한 음악 타입 (슬플 때 어떤 음악을 듣는지, 화날 때 어떤 음악을 듣는지) 반환

- 유튜브 플레이어 모듈
>class YoutubeConfig : API_KEY를 저장해 두는 클래스
>>class YouTubePlayerFrag extends YouTubeBaseActivity : 추천 받은 음악의 YouTube 주소 고유 ID를 사용하여 영상 재생을 위한 준비 및 영상 재생을 하는 클래스

- 캘린더 모듈
>CalendarFrag
void getMonthlyData() : 클릭한 달의 기분 통계 값을 서버로부터 조회
void getTotalData() : 전체 기간 동안의 기분 통계 값을 서버로부터 조회
setMonthlyGraph() : getMonthlyData에서 받아온 퍼센트값을 이용해 그래프 생성 
setTotalGraph() :  getTotalData에서 받아온 퍼센트값을 이용해 그래프 생성 
	>CalPopupFrag
class getPopupData extends Thread : 캘린더에서 클릭한 날짜에 추천받았던 음악 두 곡 조회
void setWeatherIcon(String weather) : 추천받은 음악에 해당하는 세부 날씨를 기반으로 날씨 아이콘을 설정

- 플레이리스트 모듈
>PlaylistFrag
void setPlaylist() : songAdapter에서 저장된 노래 리스트를 플레이리스트 화면에 보여줌
void setFilter() : 선택된 날씨/기분 필터에 따라 해당 날씨/기분일 때 추천받았고 좋아요를 눌렀던 노래들 목록을 필터링 해서 플레이리스트에 저장
void get_thumbup_pl() : 좋아요 누른 노래 목록을 서버에서 받아옴
void set_weather_filter() : 좋아요를 누른 날씨 기반 노래 목록을 서버에서 받아옴
void set_feeling_filter() : 좋아요를 누른 기분 기반 노래 목록을 서버에서 받아옴
>SongAdapter
public MyView onCreateViewHolder() : ViewHolder가 생성되는 함수
public void onBindViewHolder() : RecyclerView에서 보여질 노래 데이터를 ViewHolder에 바인딩해주는 함수
public int getItemCount() : RecyclerView에 보여줄 데이터의 전체 길이를 반환하는 함수
>Song
int getId() : Song 클래스의 id를 반환하는 함수
String getTitle() : Song 클래스의 title을 반환하는 함수
String getSinger() : Song 클래스의 singer를 반환하는 함수
String getImg_url() : Song 클래스의 img_url을 반환하는 함수
void setId() : Song 클래스의 id를 설정하는 함수

## 백엔드
- 날씨 기반 추천 모듈
>  날씨 조회 API
  >  dev.evertime.shop/weather
geolocation(params, (err, data)): geolocation api를 기반으로 얻은 위도와 경도 조회
getCurrentWeatherByGeoCoordinates(lat, lng, (err, currentWeather) : openWeatherOpenApi 를 사용하여 날씨 조회
setWeather : 날씨 정보 서버에 저장
updateWeather : 날씨 정보 서버에 업데이트
   >  dev.evertime.shop/weather/music
isExistWeatherMusic : 날씨 기반 음악 추천 받은지 유무 확인
retrieveWeather : 오늘의 저장 된 날씨를 서버에서 조회
retrieveWeatherMusic1: 약한 천둥, 비를 동반한 천둥, 흐린 구름, 강한 눈, 약한 비의 날씨일 때 음악 추천
retrieveWeatherMusic2 : 강한 천둥, 매서운 천둥, 뇌우, 토네이도, 강한 비가 동반한 날씨일 때 음악 추천
retrieveWeatherMusic3 : 약한 이슬비, 이슬비, 적은 구름, 안개, 미스트, 약한 눈이 동반한 날씨일 때 음악 추천
retrieveWeatherMusic2 : 강한 천둥, 매서운 천둥, 뇌우가 동반한 날씨일 때 음악 추천
retrieveWeatherMusic4 : 맑은 하늘인 날씨일 때 음악 추천
retrieveYoutubeUrl : Youtube API를 연동하여 추천 음악 정보 조회
settingRecommend : 오늘의 추천 노래로 서버에 저장

- 기분 기반 추천 모듈
> dev.evertime.shop/feeling/:num
setFeeling : 클라이언트로부터 얻은 숫자를 오늘의 기분으로 저장
updateFeeling: 클라이언트로부터 받은 숫자를 오늘의 기분으로 다시 업데이트

> dev.evertime.shop/feeling/music
retrieveFeelingMusic1 : 기분이 Exciting, Happy 일 때 추천 음악 제공
retrieveYoutubeUrl2 : Youtube API를 연동하여 추천 음악 정보 조회
updateYoutubeInfo :  Youtube 동영상 URL 과 이미지 URL 을 서버 저장
settingRecommend2 : 오늘의 추천 노래로 서버에 저장
retrieveFeelingMusic2 : 기분이 So So 일 때 추천 음악 제공
retrieveSadType : 슬플 때 어떤 장르의 곡을 들을지 사용자 타입 조회
retrieveFeelingMusic3 : 기분이 Sad 일 때 추천 음악 제공
retrieveFeelingMusic4 : 기분이 Sad 일 때 사용자 타입(밝은 노래 취향) 기반 음악 제공
retrieveAngryType : 화날 때 어떤 장르의 곡을 들을지 사용자 타입 조회
retrieveFeelingMusic5 : 기분이 Angry 일 때 추천 음악 제공

- 사용자 모듈
> dev.evertime.shop/user/:type1/:type2
existUser : 사용자의 기분 별 노래 장르 유형 유무 조회
setType: 사용자가 슬플 때 듣는 노래 장르 유형 저장
updateType: 사용자가 슬플 때 듣는 노래 장르 업데이트 저장

- 캘린더 모듈
> dev.evertime.shop/music/year/:year/mon/:mon/day/:day
retrieveMusicList1, 2: 년도, 월, 일 별마다 날씨 기반의 추천 음악과, 기분 기반의 추천음악 조회
> dev.evertime.shop/feeling/chart/:month
retrieveGetChart : 월별로 기분 상태 통계
> dev.evertime.shop/feeling/totalchart
retrieveTotalGetChart : 기분 상태 전체 통계

- 플레이리스트 모듈
> dev.evertime.shop/playlist
retrievePlaylistTotal : 전체 플레이리스트 음악 조회
> dev.evertime.shop/playlist/weather/:num
retrievePlaylistWeather1 : 천둥을 동반한 날씨일 때 좋아요를 눌렀던 노래목록 조회
retrievePlaylistWeather2 : 이슬비를 동반한 날씨일 때 좋아요를 눌렀던 노래목록 조회
retrievePlaylistWeather3 : 비를 동반한 날씨일 때 좋아요를 눌렀던 노래목록 조회
retrievePlaylistWeather4 : 눈을 동반한 날씨일 때 좋아요를 눌렀던 노래목록 조회
retrievePlaylistWeather5 : 미스트, 안개, 황사일 때 좋아요를 눌렀던 노래목록 조회
retrievePlaylistWeather6 : 맑은 하늘일 때 좋아요를 눌렀던 노래목록 조회
retrievePlaylistWeather7 : 구름이 적을 때 좋아요를 눌렀던 노래목록 조회
> dev.evertime.shop/playlist/feeling/:num
retrievePlaylistFeeling: 입력 받은 기분 인덱스를 기반으로, 해당 기분일 때 좋아요를 눌렀던 노래목록 조회

# 4. 각 기능 별 결과 화면


