Tóm tắt về Service component trong Android
1. Service là một component trong Android, được dùng để chạy các task vụ ngầm mà không yêu cầu UI.
2. Nếu so sánh với Thread, thì Service có thể chạy kể cả khi user chuyển sang dùng một App khác.
3. Một Android component khác có thể bind tới một service và tương tác với service đó/thực hiện Interprocess Communication (IPC)
4. Một service chạy trên main-thread của process cha của nó. Mình có thể chỉ định một Service chạy trên process khác (từ khóa trong manifest: android:process=":newprocess")
5. Vì service chạy trên main-thread, nên khi thực hiện các blocking/long operations thì chúng ta hãy thực hiện những operations đó trên một background thread khác --> Tránh ANR
6. Các loại service:
    6.1.1: Foreground service: Là service có kèm theo một Notification để người dùng nhận biết rằng nó đang chạy.
                            Ví dụ như một Audio application
                            Chúng ta chỉ nên dùng foreground service khi App cần thực hiện task mà task đó có thể thông báo tới user ngay cả khi user không tương tác với app.
                            Nếu hành động trên không đủ quan trọng thì chúng ta có thể thay thế bằng background task.
    6.1.2: Background service: Thực hiện các tác vụ không trực tiếp thông báo tới người dùng.
                            Ví dụ như một Cleaning application
    6.2.1: Started service: Là service được chạy bằng lệnh startService() (onStartCommand() sẽ được gọi), và bị hủy bằng stopService()/stopSelf()
    6.2.2: Bound service: Là service khi có một thằng Android component khác bind tới qua method .bindService() (onBind() sẽ được gọi)
                          Bound Service cung cấp giao diện client-server để chúng có thể trao đổi các thông tin với nhau.
                          Một Bound Service chỉ được chạy khi có ít nhất một Android component bind tới nó.
                          Và Bound service bị hủy khi tất cả các clients unbind tới nó.
    Một service có thể vừa là started service, vừa là bound service.
7. Nếu chỉ muốn service mình tạo dùng trong Application của mình, thì mình set cờ như sau: android:exported="false"
8. Các callback cần được override cho Service:
    11.1: onStartCommand(): Được gọi khi App component khác startService().
                            Khi method này được gọi, một service được started và chạy vô hạn ở background.
                            Bạn phải có trách nhiệm gọi stopSelf()/stopService() khi công việc hoàn thành.
                            Nếu bạn chỉ muốn cung cấp cơ chế binding, thì có thể ko cần implement hàm này.

    11.2: onBind()        : Được gọi khi App component khác gọi bindService() tới Service này.
                            Trong hàm này, bạn phải cung cấp một IBinder interface mà clients dùng để giao tiếp với service này.
                            Nếu không muốn cho phép cơ chế binding, bạn nên return null. Còn không thì hãy luôn implement hàm này.

    11.3: onCreate()      : The System gọi hàm này để thực hiện một lần quá trình khởi tạo Service (chạy trước onStartCommand() và onBind())
                            Nếu Service đang chạy, thì hàm này sẽ không được gọi nữa.

    11.4: onDestroy()     : The System gọi hàm này khi Service không còn được dùng nữa và sẽ bị hủy.
                            Hàm này được dùng để clean up any resources: Thread, register listeners, or receivers.

9. Note: Để app của bạn được bảo mật, thì luôn luôn dùng Explicit Intent để start một Service, và không khai báo Intent-Filter cho service đó.
10. Android Framework cũng cung cấp IntentService, dùng worker-thread để xử lý tất cả các start requests cùng một lúc. Nhưng từ Android 11, thì được khuyến nghị thay thế
    bằng JobIntentService.
11. Service Lifecycle
A Started Service: là service được tạo khi một components khác gọi startService(). Service này sẽ được chạy mãi mãi và phải từ gọi stopSelf() để dừng lại khi xong việc.
                    hoặc components khác gọi stopService(). Khi Service bị dừng lại, system sẽ destroy service này đi.
A Bounded Service: là service được tạo khi một components khác (client) gọi bindService(). Clients sau đó sẽ giao tiếp với Service này thông qua một IBinder interface.
                    Client có thể close connection bằng cách gọi unbindService(). Nhiều clients có thể bind tới cùng một service cùng lúc, và sau khi tất cả gọi unbindService()
                    thì system sẽ destroy service này đi. Service không cần phải tự gọi stop.
Chúng ta có thể bind tới một started service đang chạy trước đó rồi. Trong trường hợp này, nếu chúng ta gọi stopSelf(), có thể service sẽ không bị hủy luôn, vì có thể vẫn còn
binder của clients tới service đó. Nó sẽ bị hủy khi cả các clients của nó unbind tới nó nữa. Khi tất cả các clients unbind tới service, thì onUnbind() của service mới được gọi.
https://developer.android.com/static/images/service_lifecycle.png

12. Intent truyền vào onStartCommand(intent1) hoặc onBind(intent2) chính là Intent được truyền vào startService(intent1) hoặc bindService(intent2)

======== Chi tiết của Bound Service ===============
1. Bound Service cung cấp cơ chế Client-server, và nó là Server, để các thằng khác bind tới nó là Client, và chúng có thể giao tiếp với nhau.
2. Một Bound Service thì chỉ sống lúc nó phục vụ thằng khác, và không chạy vô thời hạn
3. Khi nhiều clients cùng bind tới một Service, hệ thống cache IBinder đó lại, onBind() sẽ chỉ được gọi duy nhất khi client đầu tiên bind() tới.
   Sau đó system sẽ trả về chính IBinder đó cho các clients tiếp theo, mà không cần gọi lại onBind().
4. Khi tất cả các clients unbind tới service đó, system destroys service.
5. Có 3 cách để tạo IBinder interface cho service của bạn:
    5.1: Extending the Binder class: (CÁCH NÀY chỉ dùng được khi client và server chung process)
            Nếu service của bạn chỉ được dùng cho application của bạn, và chạy trên cùng process với client, mình nên dùng cách này.
          Cách dùng: Tạo một lớp mở rộng của class Binder, và một method để trả về Service hiện tại (Service.this). Trong onBind()
            trả về instance của Binder. Client sẽ nhận được instance của Binder, và lấy Service tương ứng qua method của lớp mở rộng Binder đó.
          Cách này thường được ưu tiên trong trường hợp Service đóng vai trò là một background worker cho application của bạn.
    5.2: Using a Messenger: (CÁCH NÀY có thể dùng được khi client và server chạy trên 2 process khác nhau, thậm chí là các app khác nhau)
                            (Messenger tạo một queue để xử lý tất cả các clients request trên single thread, nên các request sẽ được thực hiện lần lượt)
            Nếu bạn cần một interface để làm việc giữa các process khác nhau, bạn có thể tạo một interface cho Service với một Mesenger.
            Theo cách này, Service sẽ defines một Handler chịu trách nhiệm phản hồi cho các loại Message khác nhau.
            Chỉ ra một Service chạy trên một process khác, ta thêm từ khóa sau vào thẻ của service đó trong file manifest.xml: android:process=":tên_process"
    5.3: Using AIDL: (CÁCH NÀY có thể dùng cho các application khác nhau giao tiếp với nhau, các request có thể thực hiện SONG SONG,
                        nhưng nội dung file .aidl giữa 2 application phải giống nhau)
            Các Calls tới một AIDL interface là direct function calls.
            - Các Calls từ local process, thì được thực thi trên cùng thread tạo ra Call đó. (Nếu mục đích của application chỉ cần như này, thì mình ko nên tạo AIDL,
                                                                                                          mà nên thay bằng việc kế thừa lớp Binder)
            - Các Calls từ remote process, thì được gửi từ một thread pool - cái mà được duy trì trong process riêng của bạn.
              Vì yếu tố thread-safe, bạn cần chuẩn bị cho các incoming calls đến đồng thời từ nhiều threads khác nhau.
              Các calls được tạo bởi một thread trên cùng một remote clients, thì sẽ được gửi lần lượt tới receiver.
            - Từ khóa "oneway" dùng để chỉnh sửa behavior of remote calls. Khi được dùng, một remote call không block, nó chỉ đơn giản là gửi data và lập tức returns.
