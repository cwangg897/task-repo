CREATE TABLE profiles (
                          id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(20) NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE profile_view_stat(
                                  id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                  profile_id BIGINT NOT NULL,
                                  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  FOREIGN KEY (profile_id) REFERENCES profiles(id)
);