package com.spring.carebookie;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UserProfileEntity {
    private UUID userProfileId;

    private String userName;

    private String userProfileImageLink;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfileEntity that = (UserProfileEntity) o;
        return Objects.equals(userProfileId, that.userProfileId)
                && Objects.equals(userName, that.userName)
                && Objects.equals(userProfileImageLink, that.userProfileImageLink);
    }

    public Optional<String> getUserProfileImageLink() {
        return Optional.ofNullable(userProfileImageLink);
    }

    public String getUserName() {
        return userName;
    }

    public UUID getUserProfileId() {
        return userProfileId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userProfileId, userName, userProfileImageLink);
    }
}
