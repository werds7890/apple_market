package com.example.apple_market

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.ClipDrawable.VERTICAL
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apple_market.databinding.ActivityDetailBinding
import com.example.apple_market.databinding.ActivityMainBinding
import com.example.apple_market.databinding.MarketItemBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.System.exit
import java.text.DecimalFormat
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var dataResult : ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.notificationBtn.setOnClickListener {
            notification()
        }

        val dataList= mutableListOf<Item>()
        dataList.add(Item(R.drawable.sample1,"산지 한달된 선풍기 팝니다ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ",
            "서울 서대문구 창천동",1000,"이사가서 필요가 없어졌어요 급하게 내놓습니다",
            "대현동",13,25,false))
        dataList.add(Item(R.drawable.sample2,"김치냉장고",
            "인천 계양구 귤현동",20000,"이사로인해 내놔요",
            "안마담",8,28,false))
        dataList.add(Item(R.drawable.sample3,"샤넬 카드지갑",
            "수성구 범어동",10000,"고퀄지갑이구요"+"\n"+"사용감이 있어서 싸게 내어둡니다",
            "코코유",23,5,false))
        dataList.add(Item(R.drawable.sample4,"금고",
            "해운대구 우제2동",10000,"금고"+"\n"+"떼서 가져가야함"+"\n"+"대우월드마크센텀"+"\n"+"미국이주관계로 싸게 팝니다",
            "Nicole",14,17,false))
        dataList.add(Item(R.drawable.sample5,"갤럭시Z플립3 팝니다",
            "연제구 연산제8동",150000,"갤럭시 Z플립3 그린 팝니다"+"\n"+"항시 케이스 씌워서 썻고 필름 한장챙겨드립니다"+"\n"+"화면에 살짝 스크래치난거 말고 크게 이상은없습니다!",
            "절명",22,9,false))
        dataList.add(Item(R.drawable.sample6,"프라다 복조리백",
            "수원시 영통구 원천동",50000,
            "까임 오염없고 상태 깨끗합니다"+"\n"+"정품여부모름",
            "미니멀하게",25,16,false))
        dataList.add(Item(R.drawable.sample7,"울산 동해오션뷰 60평 복층 펜트하우스 1일 숙박권 펜션 힐링 숙소 별장",
            "남구 옥동",150000,
            "울산 동해바다뷰 60평 복층 펜트하우스 1일 숙박권"+"\n"+"(에어컨이 없기에 낮은 가격으로 변경했으며 " +
                    "8월 초 가장 더운날 다녀가신 분 경우 시원했다고 잘 지내다 가셨습니다)"+"\n"+"1. 인원: 6명 기준입니다. 1인 10,000원 추가요금"+"\n"+"2. " +
                    "장소: 북구 블루마시티, 32-33층"+"\n"+"3. 취사도구, 침구류, 세면도구, 드라이기 2개, 선풍기 4대 구비"+"\n"+"4. 예약방법: " +
                    "예약금 50,000원 하시면 저희는 명함을 드리며 입실 오전 잔금 입금하시면 저희는 동.호수를 알려드리며 고객님은 예약자분 신분증 앞면 주민번호 뒷자리 가리시거나 지우시고 문자로 보내주시면 저희는 카드키를 우편함에 놓아 둡니다."+"\n"+"5. 33층 " +
                    "옥상 야외 테라스 있음, 가스버너 있음"+"\n"+"6. 고기 굽기 가능"+"\n"+"7. 입실 오후 3시, 오전 11시 퇴실, " +
                    "정리, 정돈 , 밸브 잠금 부탁드립니다."+"\n"+"8. 층간소음 주의 부탁드립니다."+"\n"+"9. 방3개, 화장실3개, 비데 3개"+"\n"+"10. 저희 집안이 쓰는 별장입니다.",
            "굿리치",30,26,false))

        val adapter=MyAdapter(dataList)
        binding.recyclerView.adapter=adapter
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(applicationContext,LinearLayout.VERTICAL))

        likeResult(dataList,adapter)

        adapter.itemClick=object : MyAdapter.ItemClick { //아이템클릭인터페이스를 익명함수로 온클릭함수를재정의
            override fun onClick(view: View, position: Int) {
                val intent=Intent(this@MainActivity,DetailActivity::class.java)
                intent.putExtra("item",dataList[position])
                intent.putExtra("itemPosition",position)
                dataResult.launch(intent)
            }
        }

        adapter.longClick=object : MyAdapter.LongClick {    //롱클릭인터페이스의 onLongClick 함수를 재정의
            override fun onLongClick(view: View, position: Int) {
                val removeBuilder=AlertDialog.Builder(this@MainActivity)
                removeBuilder.setTitle("상품 목록 삭제")
                removeBuilder.setMessage("정말 삭제 하시겠습니까?")
                removeBuilder.setIcon(R.drawable.clock)

                removeBuilder.setPositiveButton("확인") {dialog, which ->
                            dataList.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            adapter.notifyItemRangeChanged(position, dataList.size)
                }
                removeBuilder.setNegativeButton("취소") {dialog, which ->
                    dialog.dismiss()
                }

                removeBuilder.show()
            }
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {     //recyclerview안의 함수를 오버라이딩?
                if(dy < 0){     //스크롤 최상단 일때 보이지 않음
                    binding.fab.visibility=View.INVISIBLE
                } else if(dy > 0){      //스크롤 아래로 내려갈 때 보임
                    binding.fab.visibility=View.VISIBLE
                    }
                }
            }
        )
        binding.fab.setOnClickListener {
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }



    private fun likeResult(item:MutableList<Item>,adapter:MyAdapter) {
        dataResult=registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            result -> if(result.resultCode== RESULT_OK){
                var like=result.data?.getBooleanExtra("likeResult",false)!!
                var position=result.data?.getIntExtra("position",0)!!
                var likeCount=result.data?.getBooleanExtra("likeCount",false)!!
                Log.v("like","${like}")
                Log.v("position","${position}")
                if(like){
                    if(likeCount){
                        item[position].isLike=true
                        item[position].likeData+=1
                    }
                }else {
                    if(likeCount){
                        item[position].isLike=false
                        item[position].likeData-=1
                    }
                }
            adapter.notifyItemChanged(position)
            }
        }
    }

    override fun onBackPressed() {
       var builder=AlertDialog.Builder(this)
        builder.setTitle("정말 나가시겠습니까?")
        builder.setMessage("선택해주세요.")
        builder.setIcon(R.drawable.clock)

        val listener=object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE -> {exit()}
                    DialogInterface.BUTTON_NEGATIVE -> {}
                    DialogInterface.BUTTON_NEUTRAL -> {}
                }
            }
        }
        builder.setPositiveButton("예",listener)
        builder.setNegativeButton("아니오",listener)
        builder.setNeutralButton("취소",listener)

        builder.show()
    }

    private fun notification() {
        val manager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder : NotificationCompat.Builder
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelId="market-channel"
            val channelName="apple market"
            val channel=NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description="apple market channel open"
                setShowBadge(true)
                val uri : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri,audioAttributes)
                enableVibration(true)
            }
            manager.createNotificationChannel(channel)

            builder=NotificationCompat.Builder(this,channelId)
        }else {
            builder=NotificationCompat.Builder(this)
        }

        val bitmap=BitmapFactory.decodeResource(resources,R.drawable.clock)
        builder.run {
            setSmallIcon(R.drawable.clock)
            setWhen(System.currentTimeMillis())
            setContentTitle("새 알람이 왔습니다.")
            setContentText("알람 내용입니다.")
            setStyle(NotificationCompat.BigTextStyle()
                .bigText("샘플 텍스트입니다" +
                    "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ"
                    ))
            setLargeIcon(bitmap)

        }
        manager.notify(0,builder.build())
    }

    fun exit() {
        super.onBackPressed()
    }
}