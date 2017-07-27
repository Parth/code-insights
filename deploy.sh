sudo add-apt-repository ppa:cwchien/gradle
sudo apt update
sudo apt install nodejs-legacy nodejs npm default-jdk default-jre gradle tmux

tmux set-option remain-on-exit on
tmux split-window 'cd frontend && npm start' 
cd backend && gradle run 
