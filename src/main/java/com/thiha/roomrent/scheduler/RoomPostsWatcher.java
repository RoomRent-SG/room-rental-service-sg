package com.thiha.roomrent.scheduler;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.thiha.roomrent.model.RoomPost;
import com.thiha.roomrent.repository.RoomPostRepository;
@Component
public class RoomPostsWatcher {
    private final RoomPostRepository roomPostRepository;
    private static final long roomPostMaxLife = TimeUnit.DAYS.toMillis(30);

    public RoomPostsWatcher(RoomPostRepository roomPostRepository){
        this.roomPostRepository = roomPostRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkAndUpdateRoomPosts(){
        Date now = new Date();
        List<RoomPost> activeRoomPosts = roomPostRepository.findAllActiveRoomPosts();
        for(RoomPost roomPost: activeRoomPosts){
            long timeDifference = now.getTime() - roomPost.getPostedAt().getTime();
            if (timeDifference >= roomPostMaxLife) {
                roomPost.setArchived(true);
                roomPostRepository.save(roomPost);
            }
        }
    }
}
