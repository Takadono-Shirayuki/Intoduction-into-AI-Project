<h1>Cấu trúc thư mục trong AI Model</h1>
<b>Maze Generator Model:</b> Chứa các model được huấn luyện cho mục đích tạo mê cung </br>
<b>Maze solving Model:</b> Chứa các model được huấn luyện cho mục đích giải mê cung </br>
<b>Tools:</b> Chứa các công cụ hỗ trợ </br>
<h2>Cấu trúc thư mục Maze solving Model</h2>
<h3>Base:</h3> Chứa các tệp mã nguồn cho việc huấn luyện, 3 Model và 1 meta Model mẫu để tham khảo: </br>
<b>MazeBotOfflineTraining.ipynb:</b> Tệp mã nguồn dùng để tối ưu hoá Model từ dữ liệu có sẵn. </br>
<b>MazeBotTraining.ipynb:</b> Tệp mã nguồn dùng để tạo và huấn luyện Model. </br>
<b>Meta_Model.ipynb:</b> Tệp mã nguồn dùng để tạo và huấn luyện Meta Model. </br>
<b>RunModel.ipynb:</b> Tệp dùng để chạy 1 model và tạo ra dataset dùng làm đầu vào huấn luyện Meta Model </br>
<b>RunModels.ipynb:</b> Tệp dùng để chạy Meta Model với mục đích quan sát thực tế Meta Model. </br>
<b>Các thư mục model01, model02, model04:</b> Các model đã được tạo trong đó chứa: </br>
<b>dataset:</b> Chứa dataset dùng cho huấn luyện Meta Model. </br>
<b>bot.info:</b> Thông tin của Model. </br>
<b>model.pth:</b> Model đã huấn luyện. </br>
<b>model_info.pkl:</b> Chứa các thông tin dùng để chạy model bao gồm: </br>
    - model_path: Đường dẫn tời tệp model.pth. </br>
    - buff: Chứa thông tin buff mà model dùng. </br>
    - tau_start: Giá trị tau bắt đầu. </br>
    - tau_end: Giá trị tau kết thúc. </br>
    - tau_decay: Hệ số suy giảm tau. </br>
    - tau_decay_exponent: Số mũ dùng để tính hệ số tau. </br>
<b>replay_buffer.pkl:</b> Chứa các replay buffer dùng để tối ưu hoá mô hình, được tạo ra khi chạy tệp MazeBotTraining.ipynb. </br>
<b>training_info.pkl:</b> Chứa thông tin về việc huấn luyện model bao gồm: </br>
    - gamma: Hệ số chiết khấu. </br>
    - learning_rate: Tốc độ học. </br>
    - weight_decay: Hệ số suy giảm trọng số. </br>
<b>Thư mục meta_model01:</b> Meta Model đã được huấn luyện trong đó chứa: </br>
<b>meta_model.info:</b> Thông tin của Meta Model. </br>
<b>meta_model.pth:</b> Meta Model đã huấn luyện. </br>
<b>models_info.pkl:</b> Chứa các thông tin dùng để chạy các Models. </br>
<h3>Trained Models:</h3>
Gồm nhiều thư mục, mỗi thư mục có cấu trúc tương tự Base thể hiện một Model hoàn chỉnh
