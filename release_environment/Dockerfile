FROM ubuntu:22.04
RUN apt-get update && apt-get install -y \
    openssh-server \
    docker.io \
    sudo \
    curl \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/* 
    #刪除 apt-get update 產生的所有暫存檔案與套件清單
RUN mkdir /var/run/sshd
RUN echo "root:123456" | chpasswd
RUN sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config
#修改 SSH 的主設定檔 /etc/ssh/sshd_config，允許 root 帳號用密碼登入。
RUN sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd
#將 SSH 登入時的 pam_loginuid 模組審查從「強制執行」改為「非強制（選填）」
EXPOSE 22

CMD ["/usr/sbin/sshd", "-D"]