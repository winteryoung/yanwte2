task :publish do
  sh "mvn clean deploy -P release"
end

# 变更版本
task :change_version, [:ver] do |t, args|
  ver = args[:ver]
  sh "mvn versions:set -DnewVersion=#{ver}"
end