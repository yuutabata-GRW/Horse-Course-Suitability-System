
erDiagram
	HORSE ||--o{ RACE_ENTRY : participates
	RACE ||--o{ RACE_ENTRY : contains
	
	HORSE {
		BIGINT id PK
		VARCHAR name
		DATE birth_date
		VARCHAR sex
	}
	
	RACE {
		BIGINT id PK
		DATE race_date
		VARCHAR venue
		VARCHAR surface
		INT distance
		VARCHAR track_condition
		VARCHAR weather
	}
	
	RACE_ENTRY {
		BIGINT id PK
		BIGINT race_id FK
		BIGINT horse_id FK
		INT frame_number
		INT horse_number
		BOOLEAN finished
		INT finish_position
		VARCHAR time_seconds
		DECIMAL last_3f
		VARCHAR running_style
	}